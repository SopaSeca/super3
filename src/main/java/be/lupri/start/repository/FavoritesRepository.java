package be.lupri.start.repository;

import be.lupri.start.datamodel.Favorite;
import org.springframework.data.repository.CrudRepository;

public interface FavoritesRepository extends CrudRepository<Favorite, Long> {
}
