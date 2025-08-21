package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "water_rates")
@Data @NoArgsConstructor
public class WaterRate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    private BigDecimal baseFlat;          // e.g., monthly base charge
    private Integer baseGal;      // optional: gallons included in base
    private LocalDate effectiveStart;
    private LocalDate effectiveEnd;// nullable for open-ended
}


