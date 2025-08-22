package wgu.edu.BrinaBright.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bill_fees")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BillFee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "bill_id", nullable = false)
    private UserBill userBill;

    @Column(name = "fee_type")
    private String feeType;

    @Column(name = "fee_amount")
    private BigDecimal feeAmount;

}
