package be.lupri.start.datamodel.super3;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dice {
    private int value1;
    private  int value2;
    private  int count;
    private Player player;
    private  boolean locked;
}
