package be.lupri.start.controllers;

import be.lupri.start.datamodel.super3.Board;
import be.lupri.start.datamodel.super3.Player;
import be.lupri.start.services.Super3Service;
import java.util.Collection;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Super3Controller {
    private final Super3Service service;
    private static final Logger LOGGER = Logger.getLogger(Super3Controller.class.getSimpleName());

    public Super3Controller(Super3Service service) {
        this.service = service;
    }


    @GetMapping("/super3")
    public String board(HttpServletRequest request, Model model) {
        Player player = getSessionPlayer(request);
        String check = checkPlayerAndBoard(player, model);
        if (check != null) {
            return check;
        }
        model.addAttribute("availableBoards", service.getAvailableBoards());
        return "/super3/newboard";

    }

    @PostMapping("/super3/login")
    public String login(HttpServletRequest request, @RequestParam("name") String name, Model model) {
        Player player = Player.builder().name(name).id("PL-" + System.currentTimeMillis()).build();
        request.getSession().setAttribute("player", player);
        Board board = service.getBoard(player);
        if (board != null) {
            model.addAttribute("board", board);
            return "/super3/board";
        }
        return newBoard(model);
    }


    @PostMapping("/super3/new")
    public String newGame(HttpServletRequest request, @RequestParam("color") String color, Model model) {
        LOGGER.info("Creating board with color " + color);
        Player player = getSessionPlayer(request);
        String check = checkPlayerAndBoard(player, model);
        if (check != null) {
            return check;
        }
        return joinedOrCreatedBoard(request, service.createBoard(player), player, color, model);
    }


    @PostMapping("/super3/join")
    public String joinGame(HttpServletRequest request, @RequestParam("boardId") Long boardId, @RequestParam("color") String color, Model model) {
        LOGGER.info("Joining board " + boardId + " with color " + color);
        Player player = getSessionPlayer(request);
        String check = checkPlayerAndBoard(player, model);
        if (check != null) {
            return check;
        }
        return joinedOrCreatedBoard(request, service.joinBoard(boardId, player), player, color, model);
    }

    private String newBoard(Model model) {
        Collection<Board> availableBoards = service.getAvailableBoards();
        model.addAttribute("availableBoards", availableBoards);
        model.addAttribute("availableColors", service.getAvailableColors());
        return "/super3/newboard";
    }

    private String joinedOrCreatedBoard(HttpServletRequest request, Board board, Player player, String color, Model model) {
        if (board != null) {
            player.setColor(color);
            request.getSession().setAttribute("player", player);
            model.addAttribute("board", board);
            return "/super3/board";
        }
        return newBoard(model);
    }

    private String checkPlayerAndBoard(Player player, Model model) {
        if (player == null) {
            return "/super3/login";
        }
        Board board = service.getBoard(player);
        if (board != null) {
            model.addAttribute("board", board);
            return "/super3/board";
        }
        return null;
    }

    private Player getSessionPlayer(HttpServletRequest request) {
        Object o = request.getSession().getAttribute("player");
        if (o instanceof Player) {
            return (Player) o;
        }
        return null;
    }

}
