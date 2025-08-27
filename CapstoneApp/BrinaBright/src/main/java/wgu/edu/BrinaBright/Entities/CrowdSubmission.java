package wgu.edu.BrinaBright.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wgu.edu.BrinaBright.Enums.RateType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "crowdsources")
public class CrowdSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // FILE upload fields
    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "stored_file_path")
    private String storedFilePath;

    // TEXT input fields
    @Column(name = "town_name")
    private String townName;

    @Enumerated(EnumType.STRING)
    @Column(name = "rate_type", nullable = false)
    private RateType rateType;


    @Column(name = "fixed_rate")
    private Boolean fixedRate;

    @Column(name = "base_rate")
    private BigDecimal baseRate;

    @Column(name = "created_at")
    private LocalDate createDate;    // e.g., bill date


    @Column(name = "notes")
    private String notes;

    @Column(name = "submission_file_uploaded")
    private boolean submittedViaUpload;

    @Column(name = "admin_verified")
    private boolean adminVerified;

    @OneToMany(mappedBy = "submission", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<CrowdFee> crowdFees = new ArrayList<>();

    @OneToMany(mappedBy = "submission", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<CrowdRateTier> crowdRates = new ArrayList<>();



}
