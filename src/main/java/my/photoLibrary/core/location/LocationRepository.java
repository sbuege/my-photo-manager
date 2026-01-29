package my.photoLibrary.core.location;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

	boolean existsByCountryAndCity(String country, String city);

	Optional<Location> findByCountryAndCity(String country, String city);
}
