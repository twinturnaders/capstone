package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "sewer_rates")
@Data
@NoArgsConstructor
public class SewerRate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    private BigDecimal baseFlat;
    private Integer baseIncludedGal;      // often 0; sewer usually flat
    private LocalDate effectiveStart;
    private LocalDate effectiveEnd;
}