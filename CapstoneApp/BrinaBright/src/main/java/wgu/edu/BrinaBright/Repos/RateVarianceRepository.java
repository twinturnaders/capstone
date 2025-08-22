package wgu.edu.BrinaBright.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wgu.edu.BrinaBright.Entities.RateVariance;
import wgu.edu.BrinaBright.Enums.CustomerClass;
import wgu.edu.BrinaBright.Enums.ScopeArea;
import wgu.edu.BrinaBright.Enums.ServiceType;

import java.time.LocalDate;
import java.util.List;

public interface RateVarianceRepository extends JpaRepository<RateVariance, Long> {
    @Query("""
    SELECT rv FROM RateVariance rv
    WHERE rv.municipality.id = :muniId
      AND rv.serviceType = :service
      AND (rv.customerClass = :cust OR rv.customerClass = 'ALL')
      AND (rv.scopeArea = :scope OR rv.scopeArea = 'ALL')
      AND rv.effectiveStart <= :billDate
      AND (rv.effectiveEnd IS NULL OR rv.effectiveEnd >= :billDate)
    ORDER BY rv.priority ASC, rv.waterRangeMin ASC
  """)
    List<RateVariance> findApplicable(
            @Param("muniId") Long municipalityId,
            @Param("service") ServiceType service,
            @Param("cust") CustomerClass customerClass,
            @Param("scope") ScopeArea scopeArea,
            @Param("billDate") LocalDate billDate);
}
