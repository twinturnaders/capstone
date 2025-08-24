package wgu.edu.BrinaBright.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wgu.edu.BrinaBright.Entities.Municipality;

import java.util.List;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

    @Query(value = """
    SELECT m.*
    FROM municipalities m
    JOIN zip_codes z ON ST_DWithin(
         CAST(m.zip_center AS geography),
         CAST(z.zip_center AS geography),
         :radius
     )
    WHERE z.zip_code = :zip
""", nativeQuery = true)
    List<Municipality> findMunicipalitiesNearZip(
            @Param("zip") String zip,
            @Param("radius") double radiusMeters
    );
}