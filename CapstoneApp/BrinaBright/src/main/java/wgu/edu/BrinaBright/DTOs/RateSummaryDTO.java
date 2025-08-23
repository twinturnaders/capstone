package wgu.edu.BrinaBright.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateSummaryDTO {
    String municipalityName;
    String county;
    String state;

    BigDecimal waterBaseRate;
    Boolean waterFixed;
    Integer waterBaseGal;
    List<RateVarianceDTO> waterVariances;

    SewerRateDTO sewerRate;

    List<FeeDTO> baseFees;
    BigDecimal estimatedWaterCharge;
    BigDecimal estimatedSewerCharge;

    public RateSummaryDTO(String name, String county, String state, BigDecimal bigDecimal, Boolean aBoolean, Integer integer, List<RateVarianceDTO> waterVariances, WaterRateDTO waterRateDTO, SewerRateDTO sewerRateDTO, List<FeeDTO> fees, BigDecimal estWater, BigDecimal estSewer) {

    }
}
