package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import wgu.edu.BrinaBright.Enums.ServiceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "user_bills",
        indexes = @Index(name="bill_user_muni_date_idx", columnList = "user_id, municipality_id, billDate"))
@Data
@NoArgsConstructor
public class UserBill {

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "paid_date")
    private LocalDate paid_date;

    private Boolean paid;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ServiceType serviceType;      // WATER or SEWER, if you store separately; or drop if you store combined

    @Column(name = "bill_date")
    private LocalDate billDate;


    @Column(name = "water_charge")
    private BigDecimal waterCharge;

    @Column(name = "sewer_charge")
    private BigDecimal sewerCharge;

    @Column(name = "water_use_amount")
    private BigDecimal waterUsage;

    @Column(name = "sewer_use_amount")
    private BigDecimal sewerUsage;

    @OneToMany(mappedBy = "userBill", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @OrderBy("feeAmount DESC")
    private List<BillFee> billFees = new ArrayList<>();
}