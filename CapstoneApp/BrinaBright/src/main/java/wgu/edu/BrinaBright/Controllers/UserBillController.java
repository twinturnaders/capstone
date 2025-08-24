package wgu.edu.BrinaBright.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import wgu.edu.BrinaBright.DTOs.UserBillDTO;
import wgu.edu.BrinaBright.Entities.Municipality;
import wgu.edu.BrinaBright.Entities.User;
import wgu.edu.BrinaBright.Entities.UserBill;
import wgu.edu.BrinaBright.Repos.FeeRepository;
import wgu.edu.BrinaBright.Repos.MunicipalityRepository;
import wgu.edu.BrinaBright.Repos.UserBillRepository;
import wgu.edu.BrinaBright.Repos.UserRepository;
import wgu.edu.BrinaBright.Services.RateCalculatorService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userbills")
@RequiredArgsConstructor
public class UserBillController {

    private final UserRepository userRepository;
    private final UserBillRepository userBillRepository;
    private final FeeRepository feeRepository;
    private final RateCalculatorService rateCalculatorService;
    private MunicipalityRepository municipalityRepository;

    @PostMapping
    public ResponseEntity<?> saveUserBill(@RequestBody UserBillDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        UserBill bill = new UserBill();
        bill.setUser(user);
        bill.setWaterUsage(BigDecimal.valueOf(dto.getWaterUsage()));
        bill.setSewerUsage(BigDecimal.valueOf(dto.getSewerUsage()));
        bill.setWaterCharge(dto.getWaterCharge());
        bill.setSewerCharge(dto.getSewerCharge());
        bill.setBillDate(dto.getBillDate() != null ? dto.getBillDate() : LocalDate.now());
        bill.setDueDate(dto.getDueDate());
        bill.setPaidDate(dto.getPaidDate());
        bill.setPaid(dto.isPaid());
        bill.setBillFees(dto.getFees());

        userBillRepository.save(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body("Bill saved");
    }

    @GetMapping
    public ResponseEntity<List<UserBillDTO>> getUserBills() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        List<UserBillDTO> result = userBillRepository.findByUserId(user.getId())
                .stream()
                .map(UserBillDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/compare")
    public ResponseEntity<?> compareUserBillToNearby(@RequestParam Integer waterUsage, @RequestParam Integer sewerUsage) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);

        Long home = user.getMunicipalityId();
        if (home == null) {
            return ResponseEntity.badRequest().body("No municipality set for user.");
        }

        Municipality municipality = municipalityRepository.findById(home).get();

        List<Municipality> nearby = municipalityRepository.findMunicipalitiesNearZip(String.valueOf(municipality.getZipCenter()), 96560.0); // 60 miles in meters

        List<Map<String, Object>> results = new ArrayList<>();

        RateCalculatorService rateCalculatorService = new RateCalculatorService();

        for (Municipality m : nearby) {
            BigDecimal estWater = rateCalculatorService.calculateWaterCharge(m, waterUsage);
            BigDecimal estSewer = rateCalculatorService.calculateSewerCharge(m, sewerUsage);

            Map<String, Object> entry = Map.of(
                    "municipality", m.getName(),
                    "state", m.getState(),
                    "county", m.getCounty(),
                    "estimatedWaterCharge", estWater,
                    "estimatedSewerCharge", estSewer,
                    "total", estWater.add(estSewer)
            );

            results.add(entry);
        }

        return ResponseEntity.ok(results);
    }
}
