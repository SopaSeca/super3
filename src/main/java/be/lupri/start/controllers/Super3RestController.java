package be.lupri.start.controllers;

import be.lupri.start.datamodel.super3.Board;
import be.lupri.start.datamodel.super3.Player;
import be.lupri.start.services.Super3Service;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Super3RestController {
    private final Super3Service service;
    private static final Logger LOGGER = Logger.getLogger(Super3RestController.class.getSimpleName());

    public Super3RestController(Super3Service service) {
        this.service = service;
    }


    @RequestMapping(path = "/rest/super3/board/pin", method = RequestMethod.GET)
    public Result<Board> setPin(HttpServletRequest request, @RequestParam("area") int area, @RequestParam("pin") int pin) {
        Result<Board> result = new Result<>();
        Player player = getSessionPlayer(request);
        Board board = service.setPin(player, area, pin);
        result.setResult(board);
        result.setMyTurn(board.getActivePlayer() != null && board.getActivePlayer().equals(player));
        return result;
    }

    @RequestMapping(path = "/rest/super3/board/", method = RequestMethod.GET)
    public Result<Board> board(HttpServletRequest request) {
        Result<Board> result = new Result<>();
        Player player = getSessionPlayer(request);
        Board board = service.getBoard(player);
        result.setResult(board);
        result.setMyTurn(board.getActivePlayer() != null && board.getActivePlayer().equals(player));
        return result;
    }

    @RequestMapping(path = "/rest/super3/board/roll", method = RequestMethod.GET)
    public Result<Board> roll(HttpServletRequest request) {
        Result<Board> result = new Result<>();
        Player player = getSessionPlayer(request);
        Board board = service.rollDice(player);
        result.setResult(board);
        result.setMyTurn(board.getActivePlayer() != null && board.getActivePlayer().equals(player));
        return result;
    }

    private Player getSessionPlayer(HttpServletRequest request) {
        Object o = request.getSession().getAttribute("player");
        if (o instanceof Player) {
            return (Player) o;
        }
        return null;
    }

    @Data
    @NoArgsConstructor
    private class Result<T> {
        T result;
        boolean myTurn;
    }

}
