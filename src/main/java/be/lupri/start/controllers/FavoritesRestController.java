package be.lupri.start.controllers;

import be.lupri.start.datamodel.Favorite;
import be.lupri.start.services.FavoritesService;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class FavoritesRestController {
    private static final Logger LOGGER = Logger.getLogger(FavoritesRestController.class.getSimpleName());

    private final FavoritesService service;

    public FavoritesRestController(FavoritesService service) {
        this.service = service;
    }

    @RequestMapping(path = "/rest/favorites", method = RequestMethod.GET)
    public Iterable<Favorite> list() {
        return service.getFavorites();
    }

    @RequestMapping(path = "/rest/favorites", method = RequestMethod.POST)
    public Favorite save(@RequestBody Favorite favorite) {
        LOGGER.info("Saving " + favorite);
        if (favorite.getUrl() == null) {
            return favorite;
        }
        return service.save(favorite);
    }

    @RequestMapping(path = "/rest/favorites/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Deleting  " + id);
        service.delete(id);
    }
}
