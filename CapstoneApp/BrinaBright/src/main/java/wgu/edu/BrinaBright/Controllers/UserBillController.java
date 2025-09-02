package wgu.edu.BrinaBright.Controllers;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import wgu.edu.BrinaBright.DTOs.UserBillDTO;
import wgu.edu.BrinaBright.DTOs.ViewBillDTO;
import wgu.edu.BrinaBright.Entities.User;
import wgu.edu.BrinaBright.Entities.UserBill;
import wgu.edu.BrinaBright.Repos.FeeRepository;
import wgu.edu.BrinaBright.Repos.MunicipalityRepository;
import wgu.edu.BrinaBright.Repos.UserBillRepository;
import wgu.edu.BrinaBright.Repos.UserRepository;
import wgu.edu.BrinaBright.Security.CurrentUser;
import wgu.edu.BrinaBright.Security.JwtTokenProvider;
import wgu.edu.BrinaBright.Security.UserPrincipal;
import wgu.edu.BrinaBright.Services.RateCalculatorService;
import wgu.edu.BrinaBright.Services.UserService;
import wgu.edu.BrinaBright.Services.ViewBillService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/userbills")
@RequiredArgsConstructor
public class UserBillController {

    private final UserRepository userRepository;
    private final UserBillRepository userBillRepository;
    private final FeeRepository feeRepository;
    private final RateCalculatorService rateCalculatorService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private MunicipalityRepository municipalityRepository;
    private final ViewBillService viewBillService;



        @GetMapping
        @PreAuthorize("isAuthenticated()")

        public ResponseEntity<List<UserBillDTO>> list(@CurrentUser User user) {
            List<UserBillDTO> rows = viewBillService.getUserBillsForUser(user.getId());
            return ResponseEntity.ok(rows);
        }



    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> saveUserBill(@CurrentUser User user, @RequestBody UserBillDTO dto) {


        {


            UserBill bill = new UserBill();


            bill.setUser(user);


            if (dto.getWaterUsage() != null) {
                bill.setWaterUsage(dto.getWaterUsage());
            }
            if (dto.getSewerUsage() != null) {
                bill.setSewerUsage(dto.getSewerUsage());
            }

            if (dto.getWaterCharge() != null) {
                bill.setWaterCharge(dto.getWaterCharge());
            }
            if (dto.getSewerCharge() != null) {
                bill.setSewerCharge(dto.getSewerCharge());
            }

            bill.setBillDate(dto.getBillDate() != null ? dto.getBillDate() : LocalDate.now());

            if (bill.getBillDate() != null) {
                bill.setDueDate(dto.getDueDate());
            }

            if (bill.getDueDate() != null) {
                bill.setPaidDate(dto.getPaidDate());
            }

            bill.setPaid(Boolean.TRUE.equals(dto.getPaid()));

            if (dto.getFees() != null) {
                bill.setBillFees(dto.getFees());
            }


            userBillRepository.save(bill);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Bill saved", "id", bill.getId()));
        }
    }
}
