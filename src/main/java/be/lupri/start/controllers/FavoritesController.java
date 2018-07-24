package be.lupri.start.controllers;

import be.lupri.start.datamodel.Favorite;
import java.util.logging.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class FavoritesController {

    private final RestTemplate restTemplate;
    private static final Logger LOGGER = Logger.getLogger(FavoritesController.class.getSimpleName());
    public FavoritesController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/favorites")
    public String favorites(Model model) {
        Favorite[] favorites = restTemplate.getForObject("http://localhost:8080/rest/favorites", Favorite[].class);
        model.addAttribute("favorites", favorites == null ? new Favorite[0] : favorites);
        return "/favorites/favorites";
    }

    @PostMapping("/favorites")
    public String save(@RequestParam("name") String name, @RequestParam("url") String url, Model model) {
        Favorite favorite = Favorite.builder().name(name).url(url).build();
        LOGGER.info("Saving " + favorite);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Favorite> request = new HttpEntity<>(favorite, headers);

        ResponseEntity<Favorite> result = restTemplate.postForEntity("http://localhost:8080/rest/favorites", request, Favorite.class);

        Favorite[] favorites = restTemplate.getForObject("http://localhost:8080/rest/favorites", Favorite[].class);
        model.addAttribute("favorites", favorites == null ? new Favorite[0] : favorites);
        return "/favorites/favorites";
    }

    @PostMapping("/favorites/delete")
    public String delete(@RequestParam("id") Long id, Model model) {
        if (id != null) {
            restTemplate.delete("http://localhost:8080/rest/favorites/" + id);
        }
        Favorite[] favorites = restTemplate.getForObject("http://localhost:8080/rest/favorites", Favorite[].class);
        model.addAttribute("favorites", favorites == null ? new Favorite[0] : favorites);
        return "/favorites/favorites";
    }
}
