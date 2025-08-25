package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "crowd_rates")
public class CrowdRateTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;

    @Column(name = "rate_min_range")
    private Integer minRange;

    @Column(name = "rate_max_range")
    private Integer maxRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private CrowdSubmission submission;
}