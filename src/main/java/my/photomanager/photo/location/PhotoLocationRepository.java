package my.photomanager.photo.location;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoLocationRepository extends JpaRepository<PhotoLocation, Integer> {

	Optional<PhotoLocation> findByCountryAndCity(@NonNull String country, @NonNull String city);

}
