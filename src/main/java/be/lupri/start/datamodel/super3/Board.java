package be.lupri.start.datamodel.super3;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Collection;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Board extends Cell {
    @Id
    private Long id;
    private Player player1;
    private Player player2;
    private Player activePlayer;
    private Dice dice;
    private Collection<String> availableColors;
}
