package be.lupri.start.services;

import be.lupri.start.datamodel.super3.Area;
import be.lupri.start.datamodel.super3.Board;
import be.lupri.start.datamodel.super3.Cell;
import be.lupri.start.datamodel.super3.Dice;
import be.lupri.start.datamodel.super3.Pin;
import be.lupri.start.datamodel.super3.Player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class Super3Service {

    private Collection<Board> boards = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(Super3Service.class.getSimpleName());
    private static long boardId = 0;

    public synchronized Collection<Board> getAvailableBoards() {
        LOGGER.info("There are " + boards.size() + "  boards");
        final List<Board> result = new ArrayList<>();
        boards.stream().filter(board -> board.getPlayer2() == null && board.getPlayer() == null).forEach(b -> {
            b.setAvailableColors(getAvailableColorsForBoard(b));
            result.add(b);
        });
        LOGGER.info("There are " + result.size() + " available boards");
        return result;
    }

    public Board getBoard(Player player) {
        LOGGER.info("getBoard");
        for (Board board : boards) {
            if (player.equals(board.getPlayer1()) || player.equals(board.getPlayer2())) {
                board.setAvailableColors(getAvailableColorsForBoard(board));
                return board;
            }
        }
        return null;
    }

    public synchronized Board joinBoard(Long boardId, Player player2) {
        LOGGER.info("joinBoard");
        Board board = null;
        for (Board b : boards) {
            if (b.getId().equals(boardId)) {
                board = b;
            }
        }

        if (board != null && board.getPlayer2() == null) {
            board.setPlayer2(player2);
            board.setAvailableColors(getAvailableColorsForBoard(board));
            board.setActivePlayer(Math.random() < 0.5 ? board.getPlayer1() : player2);
            return board;
        }
        return null;
    }

    private Collection<String> availableColors;

    public Collection<String> getAvailableColors() {
        if (availableColors == null) {
            availableColors = new HashSet<>();
            availableColors.add("color-red");
            availableColors.add("color-green");
            availableColors.add("color-yellow");
            availableColors.add("color-blue");
        }
        return availableColors;
    }

    public Collection<String> getAvailableColorsForBoard(Board board) {
        Collection<String> availableColors = new HashSet<>(getAvailableColors());
        if (board.getPlayer1() != null && board.getPlayer1().getColor() != null) {
            availableColors.remove(board.getPlayer1().getColor());
        }
        if (board.getPlayer2() != null && board.getPlayer2().getColor() != null) {
            availableColors.remove(board.getPlayer2().getColor());
        }
        return availableColors;

    }

    public synchronized Board createBoard(Player player) {
        LOGGER.info("createBoard");
        Board board = new Board();
        board.setType("Board");
        board.setId(boardId++);
        board.setCells(initAreas());
        board.setPlayer1(player);
        board.setAvailableColors(getAvailableColorsForBoard(board));
        boards.add(board);

        checkMatrix(board);
        return board;
    }

    public Board endBoard(Player player) {
        LOGGER.info("endBoard");
        for (Board board : boards) {
            if (player.equals(board.getPlayer1())) {
                board.setPlayer1(null);
                if (board.getPlayer() == null) {
                    board.setPlayer(board.getPlayer2());
                }
                return board;
            }
            if (player.equals(board.getPlayer2())) {
                board.setPlayer2(null);
                if (board.getPlayer() == null) {
                    board.setPlayer(board.getPlayer1());
                }
                return board;
            }
        }
        return null;
    }


    public synchronized Board rollDice(Player player) {
        LOGGER.info("rollDice");
        Board board = getBoard(player);
        if (board != null) {
            Dice dice = board.getDice();
            if (dice == null) {
                dice = new Dice();
                board.setDice(dice);
                board.getDice().setLocked(false);
            }
            if (!dice.isLocked() && player.equals(board.getActivePlayer())) {
                if (player.equals(dice.getPlayer())) {
                    dice.setCount(dice.getCount() + 1);
                } else {
                    dice.setCount(1);
                }
                dice.setValue1(ThreadLocalRandom.current().nextInt(1, 6 + 1));
                dice.setValue2(ThreadLocalRandom.current().nextInt(1, 6 + 1));
                dice.setPlayer(player);

                if (canPlay(board)) {
                    dice.setLocked(true);
                } else {
                    dice.setLocked(dice.getCount() == 3);
                }
            } else {
                LOGGER.info("Did not set the pin: ");
            }
        }
        return board;
    }

    private boolean canPlay(Board board) {
        Dice dice = board.getDice();
        if (dice != null) {
            int value = dice.getValue1() + dice.getValue2();
            for (Cell area : board.getCells()) {
                if (area.getPlayer() == null) {
                    for (Cell pin : area.getCells()) {
                        if (value == 2) {
                            if (pin.getPlayer() != null && !pin.getPlayer().equals(dice.getPlayer())) {
                                return true;
                            }
                        } else {
                            if (pin.getPlayer() == null) {
                                if (value == 12) {
                                    return true;
                                }
                                if (area.getNumber() == value || pin.getNumber() == value) {
                                    if (value == 7) {
                                        return true;
                                    }
                                    if (pin.getNumber() != 7) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public synchronized Board setPin(Player player, int areaNumber, int pinNumber) {
        LOGGER.info("Set pin " + player + "@" + areaNumber + ":" + pinNumber);
        Board board = getBoard(player);
        if (board != null) {
            Cell area = board.getCells()[areaNumber - 3];
            if (area instanceof Area && area.getPlayer() == null) {
                Cell pin = area.getCells()[pinNumber - 3];
                if (pin instanceof Pin) {
                    if (board.getDice() != null) {
                        int value = board.getDice().getValue1() + board.getDice().getValue2();
                        LOGGER.info("Checking value " + value + " @" + area.getNumber() + "-" + pin.getNumber());
                        if (value == 2) {
                            if (pin.getPlayer() != null && !pin.getPlayer().equals(player)) {
                                pin.setPlayer(null);
                            } else {
                                LOGGER.info("Can't remove empty or own player");
                                return board;
                            }
                        } else if (pin.getPlayer() == null) {
                            if (value == 12) {
                                pin.setPlayer(player);
                            } else if (area.getNumber() == value || pin.getNumber() == value) {
                                if (value != 7 || pin.getNumber() == 7) {
                                    pin.setPlayer(player);
                                }
                            } else {
                                LOGGER.info("Area or pin is not the same as the value");
                                return board;
                            }
                        }
                        checkMatrix(board);
                        board.setActivePlayer(player.equals(board.getPlayer1()) ? board.getPlayer2() : board.getPlayer1());
                        board.getDice().setLocked(false);
                    }
                }
            }
        } else {
            LOGGER.info("The player is not playing.");
        }
        return board;
    }


    private void checkMatrix(Cell matrix) {
        LOGGER.info("Checking " + matrix.getType());
        if (matrix.getPlayer() == null) {
            if (matrix.getCells() != null && matrix.getCells().length >= 9) {
                for (Cell cell : matrix.getCells()) {
                    checkMatrix(cell);
                }
                checkRow(matrix, 0);
                checkRow(matrix, 1);
                checkRow(matrix, 2);
                checkCombo(matrix, 0, 4, 8);
                checkCombo(matrix, 2, 4, 6);
                checkCol(matrix, 0);
                checkCol(matrix, 1);
                checkCol(matrix, 2);
                if (matrix.getPlayer() == null) {
                    if (matrix.getCells() != null) {
                        int player1Count = 0, player2Count = 0;
                        Player player1 = null, player2 = null;
                        for (Cell cell : matrix.getCells()) {
                            if (matrix.getPlayer() != null) {
                                if (player1 == null) {
                                    player1 = cell.getPlayer();
                                    player1Count++;
                                } else if (cell.getPlayer().equals(player1)) {
                                    player1Count++;
                                } else {
                                    player2Count++;
                                    if (player2 == null) {
                                        player2 = cell.getPlayer();
                                    }
                                }
                            }
                        }
                        if (player1Count >= 5) {
                            matrix.setPlayer(player1);
                        } else if (player2Count >= 5) {
                            matrix.setPlayer(player2);
                        }
                    }
                }
            }
        }
    }

    private void setOwner(Cell matrix, Player player) {
        if (matrix.getPlayer() == null) {
            matrix.setPlayer(player);
            for (Cell pin : matrix.getCells()) {
                pin.setPlayer(player);
            }
        }
    }

    private boolean checkCell(Player player, Player player2) {
        if (player == null) {
            return false;
        }
        return player.equals(player2);
    }

    private void checkCombo(Cell matrix, int a, int b, int c) {
        if (matrix.getPlayer() == null) {
            if (checkCell(matrix.getCells()[a].getPlayer(), matrix.getCells()[b].getPlayer()) &&
                    checkCell(matrix.getCells()[a].getPlayer(), matrix.getCells()[c].getPlayer())) {
                setOwner(matrix, matrix.getCells()[a].getPlayer());
            }
        }
    }

    private void checkRow(Cell matrix, int row) {
        if (matrix.getPlayer() == null) {
            int offset = row * 3;
            checkCombo(matrix, offset, offset + 1, offset + 2);
        }
    }

    private void checkCol(Cell matrix, int col) {
        if (matrix.getPlayer() == null) {
            checkCombo(matrix, col, col + 3, col + 6);
        }
    }

    private Area[] initAreas() {
        int number = 3;
        return new Area[]{
                initArea(number++)
                , initArea(number++)
                , initArea(number++)
                , initArea(number++)
                , initArea(number++)
                , initArea(number++)
                , initArea(number++)
                , initArea(number++)
                , initArea(number++)
        };
    }

    private Area initArea(int number) {
        Area area = Area.builder().build();
        area.setCells(initPins());
        area.setNumber(number);
        area.setType("Area");
        return area;
    }

    private Pin[] initPins() {
        int number = 3;
        return new Pin[]{
                initPins(number++)
                , initPins(number++)
                , initPins(number++)
                , initPins(number++)
                , initPins(number++)
                , initPins(number++)
                , initPins(number++)
                , initPins(number++)
                , initPins(number++)
        };
    }

    private Pin initPins(int number) {
        Pin pin = Pin.builder().build();
        pin.setNumber(number);
        pin.setType("Pin");
        return pin;
    }
}
