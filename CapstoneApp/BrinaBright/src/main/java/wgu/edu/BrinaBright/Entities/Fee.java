package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "fees")
@Data
@NoArgsConstructor
public class Fee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    @Column(name = "fee_policy")
    private String feePolicy; // ex: late fee applied if more than 10 days late

    @Column(name = "fee_amount")
    private BigDecimal amount;

    @Column(name = "base_fee")
    private Boolean baseFee;                // if true, always added to charges

    @Column(name="fee_description")
    private String feeType;

    @Column(name = "is_percentage")
    private Boolean isPercentage; //if true need total bill amount to calculate

    @Column(name="percentage_amount")
    private BigDecimal percentage;

    @Column(name = "created_at")
    private LocalDate effectiveStart;

    @Column(name = "end_date")
    private LocalDate effectiveEnd;
}
