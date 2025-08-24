package wgu.edu.BrinaBright.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wgu.edu.BrinaBright.Entities.BillFee;
import wgu.edu.BrinaBright.Entities.UserBill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBillDTO {
    private Integer waterUsage;     // Gallons
    private Integer sewerUsage;     // Gallons

    private BigDecimal waterCharge;
    private BigDecimal sewerCharge;

    private LocalDate billDate;
    private LocalDate dueDate;
    private LocalDate paidDate;

    private boolean paid;

    // Example: {"lateFee": 15.00, "deposit": 75.00}
    private Map<String, BigDecimal> fees;


    public UserBillDTO(BigDecimal waterUsage, BigDecimal sewerUsage, BigDecimal waterCharge, BigDecimal sewerCharge, LocalDate billDate, LocalDate dueDate, LocalDate paidDate, boolean b, Map<String, BigDecimal> fees) {

    }

    public static UserBillDTO fromEntity(UserBill bill) {
        return new UserBillDTO(
                bill.getWaterUsage(),
                bill.getSewerUsage(),
                bill.getWaterCharge(),
                bill.getSewerCharge(),
                bill.getBillDate(),
                bill.getDueDate(),
                bill.getPaidDate(),
                bill.billPaid(),
                bill.getFees()
        );
    }


}
