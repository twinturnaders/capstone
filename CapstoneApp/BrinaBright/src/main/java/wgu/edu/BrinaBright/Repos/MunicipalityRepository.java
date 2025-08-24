package wgu.edu.BrinaBright.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wgu.edu.BrinaBright.Entities.Municipality;

import java.util.List;
import java.util.Optional;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    //find municipality on registration
    @Query(value = """
      SELECT m.* 
      FROM municipalities m
      JOIN zip_codes z ON z.zip_code = :zip
       WHERE ST_DWithin(
              CAST(m.zip_center AS geography),
              CAST(z.zip_center AS geography),
              :radius
          )
      LIMIT 1
      """, nativeQuery = true)
    Optional<Municipality> findContainingZip(String zip);

    // B) Nearest municipality to the ZIP point (KNN)
    @Query(value = """
      SELECT m.* 
      FROM municipalities m
      JOIN zip_codes z ON z.zip_code = :zip
             WHERE ST_DWithin(
                    CAST(m.zip_center AS geography),
                    CAST(z.zip_center AS geography),
                    :radius
                )
      ORDER BY m.zip_center <-> z.zip_center
      LIMIT 1
      """, nativeQuery = true)
    Optional<Municipality> findNearestToZip(String zip);

    //query by distance(user configured)
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
//Order by distance:

    @Query(value = """
    SELECT m.*
    FROM municipalities m
    JOIN zip_codes z ON z.zip_code = :zip
    WHERE ST_DWithin(
        CAST(m.zip_center AS geography),
        CAST(z.zip_center AS geography),
        :radius
    )
    ORDER BY CAST(m.zip_center AS geography) <-> CAST(z.zip_center AS geography)
""", nativeQuery = true)
    List<Municipality> findMunicipalitiesNearZipOrderedByDistance(
            @Param("zip") String zip,
            @Param("radius") double radiusMeters
    );


        @Query(value = """
        SELECT m.id, ST_Distance(
            CAST(m.zip_center AS geography),
            CAST(z.zip_center AS geography)
        ) AS distance
        FROM municipalities m
        JOIN zip_codes z ON z.zip_code = :zip
        WHERE ST_DWithin(
            CAST(m.zip_center AS geography),
            CAST(z.zip_center AS geography),
            :radius
        )
        ORDER BY distance ASC
        """, nativeQuery = true)
        List<Object[]> findMunicipalityIdsAndDistance(@Param("zip") String zip, @Param("radius") double radiusMeters);
    }


