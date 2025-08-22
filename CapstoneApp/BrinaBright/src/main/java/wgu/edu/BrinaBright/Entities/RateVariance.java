package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import wgu.edu.BrinaBright.Enums.CustomerClass;
import wgu.edu.BrinaBright.Enums.PricingModel;
import wgu.edu.BrinaBright.Enums.ScopeArea;
import wgu.edu.BrinaBright.Enums.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity @Table(name = "rate_variances",
        indexes = {
                @Index(name="rv_muni_service_idx", columnList = "municipality_id, serviceType, customerClass, scopeArea, effectiveStart"),
                @Index(name="rv_muni_priority_idx", columnList = "municipality_id, serviceType, priority")
        }
)
@Data
@NoArgsConstructor
public class RateVariance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wflat_rate_range"  )
    private Boolean waterFlatRateRange;

    @Column(name = "water_ppu")
    private BigDecimal waterPPU;

    @Column(name = "wrange_min")
    private int waterRangeMin;

    @Column(name = "wrange_max")
    private int waterRangeMax;

    @Column(name = "sewer_ppu")
    private BigDecimal sewerPPU;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ServiceType serviceType;      // WATER or SEWER

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private CustomerClass customerClass = CustomerClass.ALL;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ScopeArea scopeArea = ScopeArea.ALL;


    @Column(name = "created_at")
    private LocalDate effectiveStart;


    @Column(name = "end_date")
    private LocalDate effectiveEnd;       // nullable



    @Column(name = "priority")
    private Integer priority;

}