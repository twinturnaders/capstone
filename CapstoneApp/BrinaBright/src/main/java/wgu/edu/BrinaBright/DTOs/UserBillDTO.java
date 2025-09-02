package wgu.edu.BrinaBright.DTOs;

import lombok.*;
import wgu.edu.BrinaBright.Entities.BillFee;
import wgu.edu.BrinaBright.Entities.UserBill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Getter
@Setter


public class UserBillDTO {
    private long id;
    private LocalDate billDate;





    private LocalDate dueDate;
    private LocalDate paidDate;

    private boolean paid;
    private BigDecimal waterUsage;     // Gallons
    private BigDecimal sewerUsage;     // Gallons

    private BigDecimal waterCharge;
    private BigDecimal sewerCharge;





    private List<BillFeeDTO> fees;




    public Boolean getPaid() {
        return paid;
    }


}
