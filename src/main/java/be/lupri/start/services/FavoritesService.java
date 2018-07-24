package be.lupri.start.services;

import be.lupri.start.datamodel.Favorite;
import be.lupri.start.repository.FavoritesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

@Service
public class FavoritesService {

    private final FavoritesRepository repository;
    private static final Logger LOGGER = Logger.getLogger(FavoritesService.class.getSimpleName());

    public FavoritesService(FavoritesRepository repository) {
        this.repository = repository;
    }

    public Iterable<Favorite> getFavorites() {
        return repository.findAll();
    }

    public Favorite save(Favorite favorite) {
        LOGGER.log(Level.INFO, "Saving " + favorite);
        return repository.save(favorite);
    }

    public void delete(Long id) {
        LOGGER.log(Level.INFO, "Deleting " + id);
        repository.deleteById(id);
    }
}
