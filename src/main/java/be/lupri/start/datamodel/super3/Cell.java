package be.lupri.start.datamodel.super3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Cell {
    private Player player;
    private int number;
    private Cell[] cells;
    private String type;
}
