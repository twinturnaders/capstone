package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "user_bills")
public class UserBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal waterCharge;
    private BigDecimal sewerCharge;
    private BigDecimal waterUsage;
    private BigDecimal sewerUsage;
    private LocalDate billDate;
    private LocalDate dueDate;
    private LocalDate paidDate;
    private BigDecimal feeAmount;
    private String feeType;

    private boolean paid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
