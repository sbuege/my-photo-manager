package my.photomanager.photo.orientation;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrientationRepository extends JpaRepository<Orientation, Long> {

	Optional<Orientation> findByName(@NonNull String name);
}
