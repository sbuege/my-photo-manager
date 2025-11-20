package my.photomanager.photo.location;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

	Optional<Location> findByCountryAndCity(@NonNull String country, @NonNull String city);
}
