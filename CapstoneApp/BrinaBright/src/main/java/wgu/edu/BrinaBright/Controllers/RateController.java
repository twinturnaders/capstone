package wgu.edu.BrinaBright.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wgu.edu.BrinaBright.DTOs.RateSummaryDTO;
import wgu.edu.BrinaBright.Services.MunicipalityService;

import java.util.List;

@RestController
@RequestMapping("/api/rates")
@RequiredArgsConstructor
public class RateController {

    private final MunicipalityService municipalityService;

    @GetMapping("/nearby")
    public ResponseEntity<List<RateSummaryDTO>> getRatesNearZip(
            @RequestParam String zip,
            @RequestParam(defaultValue = "50") double radiusMiles,
            @RequestParam(defaultValue = "5000") int usageGallons
    ) {
        double radiusMeters = radiusMiles * 1609.34;
        List<RateSummaryDTO> results = municipalityService.findNearbyRates(zip, radiusMeters, usageGallons);
        return ResponseEntity.ok(results);
    }
}
