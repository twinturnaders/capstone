package wgu.edu.BrinaBright.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wgu.edu.BrinaBright.DTOs.CrowdSubmissionDTO;
import wgu.edu.BrinaBright.Security.UserPrincipal;
import wgu.edu.BrinaBright.Services.CrowdSubmissionService;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class CrowdSubmissionController {

    private final CrowdSubmissionService crowdSubmissionService;

    @PostMapping("/submit")
    public ResponseEntity<?> submitStructuredData(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody CrowdSubmissionDTO dto
    ) {
        crowdSubmissionService.saveStructuredSubmission(user.getId(), dto);
        return ResponseEntity.ok("Submission received!");
    }
}

