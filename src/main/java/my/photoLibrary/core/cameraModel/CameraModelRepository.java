package my.photoLibrary.core.cameraModel;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraModelRepository extends JpaRepository<CameraModel, Long> {

	boolean existsByName(String name);

	Optional<CameraModel> findByName(String name);
}
