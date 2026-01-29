package my.photoLibrary.core.orientation;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrientationRepository extends JpaRepository<Orientation, Long> {

	boolean existsByName(String name);

	Optional<Orientation> findByName(String name);
}
