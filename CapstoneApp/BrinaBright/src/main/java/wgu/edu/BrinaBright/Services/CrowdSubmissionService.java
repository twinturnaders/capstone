package wgu.edu.BrinaBright.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wgu.edu.BrinaBright.DTOs.CrowdSubmissionDTO;
import wgu.edu.BrinaBright.Entities.*;
import wgu.edu.BrinaBright.Enums.FeeType;
import wgu.edu.BrinaBright.Repos.SubmissionRepository;
import wgu.edu.BrinaBright.Repos.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CrowdSubmissionService {

    private final UserRepository userRepo;
    private final SubmissionRepository subRepo;

    public void saveStructuredSubmission(Long userId, CrowdSubmissionDTO dto) {
        CrowdSubmission submission = new CrowdSubmission();
        submission.setUser(userRepo.findById(userId).orElseThrow());
        submission.setTownName(dto.getTownName());
        submission.setRateType(dto.getRateType());
        submission.setBaseRate(dto.getBaseRate());
        submission.setNotes(dto.getNotes());
        submission.setSubmittedViaUpload(false);
        submission.setCreateDate(LocalDate.from(LocalDateTime.now()));

        // Attach fees
        List<CrowdFee> fees = dto.getFees().stream().map(feeDTO -> {
            CrowdFee fee = new CrowdFee();
            fee.setFeeType(FeeType.valueOf(feeDTO.feeType()));
            fee.setAmount(feeDTO.amount());
            fee.setSubmission(submission);
            return fee;
        }).toList();

        // Attach rate tiers
        List<CrowdRateTier> tiers = dto.getRateTiers().stream().map(tierDTO -> {
            CrowdRateTier tier = new CrowdRateTier();
            tier.setPricePerUnit(tierDTO.pricePerUnit());
            tier.setMinRange(tierDTO.minRange());
            tier.setMaxRange(tierDTO.maxRange());
            tier.setSubmission(submission);
            return tier;
        }).toList();

        submission.setCrowdFees(fees);
        submission.setCrowdRates(tiers);

        subRepo.save(submission);
    }
}
