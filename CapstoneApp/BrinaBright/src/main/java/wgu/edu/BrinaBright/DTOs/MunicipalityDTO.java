package wgu.edu.BrinaBright.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wgu.edu.BrinaBright.Entities.Municipality;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MunicipalityDTO {
    private String name;
    private String county;
    private String state;

    public static MunicipalityDTO fromEntity(Municipality m) {
        return new MunicipalityDTO(
                m.getName(),
                m.getCounty(),
                m.getState()
        );
    }


}
