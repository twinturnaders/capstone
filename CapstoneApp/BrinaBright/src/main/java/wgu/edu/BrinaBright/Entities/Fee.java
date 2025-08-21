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

    private String feePolicy; // ex: late fee applied if more than 10 days late
    private BigDecimal amount;
    private Boolean baseFee;                // if true, always added to charges
    private String feeType;
    private Boolean isPercentage; //if true need total bill amount to calculate
    private BigDecimal percentage;
    private LocalDate effectiveStart;
    private LocalDate effectiveEnd;
}
