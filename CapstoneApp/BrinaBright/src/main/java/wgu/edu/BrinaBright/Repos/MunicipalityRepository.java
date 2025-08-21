package wgu.edu.BrinaBright.Repos;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBReader;
import org.locationtech.jts.io.WKBWriter;
import org.springframework.data.jpa.repository.JpaRepository;
import wgu.edu.BrinaBright.Entities.Municipality;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {

    public static class Municipality {
        public long id;
        public String name;
        public String zipCenter;
        public Geometry geoBounds;
    }

   //all municipalities loaded into list
    public static List<Municipality> loadMunicipalities(Connection conn) throws Exception {
        String sql = "SELECT id, name, zip_center, ST_AsBinary(geo_bounds) AS geom_wkb FROM municipalities";
        List<Municipality> out = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            WKBReader reader = new WKBReader();

            while (rs.next()) {
                Municipality m = new Municipality();
                m.id = rs.getLong("id");
                m.name = rs.getString("name");
                m.zipCenter = rs.getString("zip_center");

                byte[] wkb = rs.getBytes("geom_wkb");
                if (wkb != null) {
                    m.geoBounds = reader.read(wkb);
                }

                out.add(m);
            }
        }
        return out;
    }

   //save/update geometry for given id
    public static void saveGeometry(Connection conn, long municipalityId, Geometry geometry) throws SQLException {
        String sql = "UPDATE municipalities SET geo_bounds = ST_SetSRID(ST_GeomFromWKB(?), 4326) WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            byte[] wkb = new WKBWriter().write(geometry);
            ps.setBytes(1, wkb);
            ps.setLong(2, municipalityId);
            ps.executeUpdate();
        }
    }
}
