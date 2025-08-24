package wgu.edu.BrinaBright.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wgu.edu.BrinaBright.Entities.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateCalculatorService {

    public BigDecimal calculateWaterCharge(Municipality m, int usageGal) {
        WaterRate rate = m.getWaterRates().stream().findFirst().orElse(null);
        if (rate == null) return BigDecimal.ZERO;

        BigDecimal total = rate.getBaseRate();
        int baseGal = rate.getBaseGal() != null ? rate.getBaseGal() : 0;
        int overage = Math.max(usageGal - baseGal, 0);

        if (!Boolean.TRUE.equals(rate.getFixedRate()) && overage > 0) {
            List<RateVariance> variances = m.getRateVariances().stream()
                    .filter(rv -> rv.getWaterPPU() != null)
                    .sorted(Comparator.comparingInt(RateVariance::getWaterRangeMin))
                    .toList();

            total = total.add(calculateTieredCharge(variances, usageGal, overage));
        }

        total = total.add(calculateBaseFees(m));
        return total.setScale(2, RoundingMode.UP);
    }

    public BigDecimal calculateSewerCharge(Municipality m, int usageGal) {
        SewerRate rate = m.getSewerRates().stream().findFirst().orElse(null);
        if (rate == null) return BigDecimal.ZERO;

        BigDecimal total = rate.getBaseRate();
        int baseGal = rate.getBaseIncludedGal() != null ? rate.getBaseIncludedGal() : 0;
        int overage = Math.max(usageGal - baseGal, 0);

        if (overage > 0) {
            List<RateVariance> variances = m.getRateVariances().stream()
                    .filter(rv -> rv.getSewerPPU() != null)
                    .sorted(Comparator.comparingInt(RateVariance::getWaterRangeMin))
                    .toList();

            for (RateVariance rv : variances) {
                int min = rv.getWaterRangeMin();
                int max = rv.getWaterRangeMax() == 0 ? Integer.MAX_VALUE : rv.getWaterRangeMax();

                if (overage > 0 && usageGal > min) {
                    int units = Math.min(overage, max - min);
                    total = total.add(rv.getSewerPPU().multiply(BigDecimal.valueOf(units)));
                    overage -= units;
                }
            }
        }

        total = total.add(calculateBaseFees(m));
        return total.setScale(2, RoundingMode.UP);
    }

    private BigDecimal calculateTieredCharge(List<RateVariance> variances, int usageGal, int overage) {
        BigDecimal additional = BigDecimal.ZERO;
        int remaining = overage;

        for (RateVariance rv : variances) {
            int min = rv.getWaterRangeMin() == null ? 0 : rv.getWaterRangeMin();
            int max = (rv.getWaterRangeMax() == null || rv.getWaterRangeMax() == 0)
                    ? Integer.MAX_VALUE
                    : rv.getWaterRangeMax();

            if (Boolean.TRUE.equals(rv.getWaterFlatRateRange())) {
                if (usageGal > min && usageGal <= max) {
                    additional = additional.add(rv.getWaterPPU());
                    break;
                }
            } else {
                int start = Math.max(min, usageGal - remaining);
                int end = Math.min(usageGal, max);

                if (end > start) {
                    int units = end - start;
                    additional = additional.add(rv.getWaterPPU().multiply(BigDecimal.valueOf(units)));
                    remaining -= units;
                }
            }
        }

        return additional;
    }

    private BigDecimal calculateBaseFees(Municipality m) {
        return m.getFees().stream()
                .filter(f -> Boolean.TRUE.equals(f.getBaseFee()))
                .map(Fee::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
