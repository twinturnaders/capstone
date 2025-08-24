package wgu.edu.BrinaBright.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import wgu.edu.BrinaBright.Entities.Municipality;
import wgu.edu.BrinaBright.Entities.User;
import wgu.edu.BrinaBright.Entities.UserBill;
import wgu.edu.BrinaBright.Repos.MunicipalityRepository;
import wgu.edu.BrinaBright.Repos.UserBillRepository;
import wgu.edu.BrinaBright.Repos.UserRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserBillRepository userBillRepository;
    private final JwtUtil jwtUtil;
    private MunicipalityRepository municipalityRepository;



    //register with pass encoder
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        String zip = request.getZipCode();
        if (zip == null || zip.isBlank()) {
            return ResponseEntity.badRequest().body("ZIP code is required");
        }

        // Prefer containment; fallback to nearest within 60 miles (~96,560 m); final fallback: pure nearest
        Municipality muni = municipalityRepository.findContainingZip(zip)
                .or(() -> municipalityRepository.findNearestToZip(zip))
                .orElseThrow(() -> new RuntimeException("No municipality found near ZIP " + zip));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setMunicipalityId(muni.getId());

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
    }

    //Creates auth token


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        //all for token refresh
        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }


    //maps auth to userbills
    @GetMapping("/userbills")
    public List<UserBill> getBillsForUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return userBillRepository.findByUserId(user.getId());
    }

    //refresh endpoint
    //May choose to store refresh tokens in database in the future
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }

        String email = jwtUtil.getEmailFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateToken(email);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }


}
