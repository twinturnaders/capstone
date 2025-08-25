package wgu.edu.BrinaBright.DTOs;

import java.math.BigDecimal;

public record CrowdRateDTO(BigDecimal pricePerUnit, Integer minRange, Integer maxRange) {}
