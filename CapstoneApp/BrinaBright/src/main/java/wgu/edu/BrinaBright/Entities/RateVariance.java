package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;


@Entity @Table(name = "rate_variances")
@Data
@NoArgsConstructor
public class RateVariance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name = "wflat_rate_range")
    private Boolean waterFlatRateRange;

    @Column(name = "water_ppu")
    private BigDecimal waterPPU;

    @Column(name = "wrange_min")
    private int waterRangeMin;

    @Column(name = "wrange_max")
    private int waterRangeMax;

    @Column(name = "sewer_ppu")
    private BigDecimal sewerPPU;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;


    @Column(name = "created_at")
    private LocalDate effectiveStart;


    @Column(name = "end_date")
    private LocalDate effectiveEnd;       // nullable


    @Column(name = "priority")
    private Integer priority;




}