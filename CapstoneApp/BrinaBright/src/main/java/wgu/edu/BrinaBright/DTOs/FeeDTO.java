package wgu.edu.BrinaBright.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wgu.edu.BrinaBright.Entities.Fee;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeDTO {
    private String type;
    private BigDecimal amount;

    public static FeeDTO fromEntity(Fee fee) {
        return new FeeDTO(fee.getFeeType(), fee.getAmount());
    }
}
