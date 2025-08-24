package wgu.edu.BrinaBright;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wgu.edu.BrinaBright.Entities.*;
import wgu.edu.BrinaBright.Services.RateCalculatorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckWaterRateTest {

    private RateCalculatorService rateCalculatorService;

    @BeforeEach
    void setup() {
        rateCalculatorService = new RateCalculatorService();
    }

    @Test
    void calculateWaterCharge_withTiersAndFee_returnsCorrectTotal() {
        // Create water rate
        WaterRate rate = new WaterRate();
        rate.setBaseRate(new BigDecimal("10.00"));
        rate.setBaseGal(0);
        rate.setFixedRate(false);

        RateVariance tier1 = new RateVariance();
        tier1.setWaterRangeMin(0);
        tier1.setWaterRangeMax(5000);
        tier1.setWaterPPU(new BigDecimal("1.00"));

        RateVariance tier2 = new RateVariance();
        tier2.setWaterRangeMin(5000);
        tier2.setWaterRangeMax(10000);
        tier2.setWaterPPU(new BigDecimal("2.00")); // $0.02 per gallon

        // Create base fee
        Fee baseFee = new Fee();
        baseFee.setAmount(new BigDecimal("5.00"));
        baseFee.setBaseFee(true);

        // Create Municipality
        Municipality muni = new Municipality();
        muni.setWaterRates(List.of(rate));
        muni.setRateVariances(List.of(tier1, tier2));
        muni.setFees(List.of(baseFee));


        int usageGal = 7500;

        BigDecimal expectedOverage = new BigDecimal("1.00").multiply(BigDecimal.valueOf(5000)) // 2,001–5,000- it will freak out if we use 2001
                .add(new BigDecimal("2.00").multiply(BigDecimal.valueOf(2500))); // 5,001–7,500- it will freak out if we use 5001

        BigDecimal expectedTotal = new BigDecimal("10.00") // base
                .add(expectedOverage)
                .add(new BigDecimal("5.00"))
                .setScale(2, RoundingMode.UP);

        BigDecimal actual = rateCalculatorService.calculateWaterCharge(muni, usageGal);

        assertEquals(expectedTotal, actual);
    }
}