package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.w3c.dom.ranges.Range;

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
    private Boolean waterFlatRateRange;
    private BigDecimal waterPPU;
    private int waterRangeMin;
    private int waterRangeMax;
    private BigDecimal sewerPPU;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ServiceType serviceType;      // WATER or SEWER

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private CustomerClass customerClass = CustomerClass.ALL;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ScopeArea scopeArea = ScopeArea.ALL;

    private LocalDate effectiveStart;
    private LocalDate effectiveEnd;       // nullable

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PricingModel pricingModel;    // INCREASING_BLOCK, etc.

    // Tier bounds (gallons). max null = open-ended.
    @Column(nullable = false) private Integer tierMinGal = 0;
    private Integer tierMaxGal;

    // One of these is used depending on pricingModel:
    private BigDecimal ratePer1k;         // per-1k price (for tiered/uniform/per-1k surcharge)
    private BigDecimal flatAmount;        // flat add-on (for FLAT_SURCHARGE)

    @Column(nullable = false) private Integer priority = 100;

    @Column(columnDefinition = "text") private String notes;
}