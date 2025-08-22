package wgu.edu.BrinaBright.Repos;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wgu.edu.BrinaBright.Entities.Municipality;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

    @Query(value = """
    SELECT m.*
    FROM municipalities m
    JOIN zip_codes z ON ST_DWithin(
        m.zip_center::geography,
        z.zip_center::geography,
        :radiusMeters
    )
    WHERE z.zip_code = :zip
""", nativeQuery = true)
    List<Municipality> findMunicipalitiesNearZip(
            @Param("zip") String zip,
            @Param("radiusMeters") double radiusMeters
    );
    }



