package wgu.edu.BrinaBright.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wgu.edu.BrinaBright.DTOs.BillFeeDTO;
import wgu.edu.BrinaBright.Repos.FeeRow;
import wgu.edu.BrinaBright.DTOs.UserBillDTO;
import wgu.edu.BrinaBright.Entities.UserBill;
import wgu.edu.BrinaBright.Repos.BillFeeRepository;
import wgu.edu.BrinaBright.Repos.UserBillRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ViewBillService {

    private final UserBillRepository userBillRepository;
    private final BillFeeRepository billFeeRepository;


    public List<UserBillDTO> getUserBillsForUser(Long userId) {
        List<UserBill> entities = userBillRepository.findByUserIdOrderByBillDateDesc(userId);
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserBillDTO> rows = entities.stream()
                .map(ViewBillService::toDto)
                .collect(Collectors.toList());


        List<Long> ids = rows.stream()
                .map(UserBillDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (ids.isEmpty()) {
            return rows;
        }

        List<FeeRow> feeRows = billFeeRepository.findRowsByUserBillIdIn(ids);


        Map<Long, List<BillFeeDTO>> feesByBill = feeRows.stream()
                .collect(Collectors.groupingBy(
                        FeeRow::getBillId,
                        Collectors.mapping(
                                fr -> new BillFeeDTO(fr.getName(), fr.getAmount()),
                                Collectors.toList())
                ));

        for (UserBillDTO dto : rows) {
            List<BillFeeDTO> feeList = feesByBill.getOrDefault(dto.getId(), Collections.emptyList());
            dto.setFees(feeList);

            BigDecimal water  = nz(dto.getWaterCharge());
            BigDecimal sewer  = nz(dto.getSewerCharge());
            BigDecimal feeSum = feeList.stream()
                    .map(BillFeeDTO::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        }

        return rows;
    }


    private static UserBillDTO toDto(UserBill ub) {
        UserBillDTO dto = new UserBillDTO();
        dto.setId(ub.getId());
        dto.setBillDate(ub.getBillDate());
        dto.setDueDate(ub.getDueDate());
        dto.setPaid(ub.getPaid());
        dto.setPaidDate(ub.getPaidDate());

        dto.setWaterUsage(ub.getWaterUsage());
        dto.setSewerUsage(ub.getSewerUsage());
        dto.setWaterCharge(ub.getWaterCharge());
        dto.setSewerCharge(ub.getSewerCharge());


        return dto;
    }


    private static BigDecimal nz(BigDecimal x) {
        return x == null ? BigDecimal.ZERO : x;
    }
}
