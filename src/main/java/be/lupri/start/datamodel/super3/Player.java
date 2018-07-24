package be.lupri.start.datamodel.super3;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Player {
    @Id
    private String id;
    private String color;
    private String name;
}
