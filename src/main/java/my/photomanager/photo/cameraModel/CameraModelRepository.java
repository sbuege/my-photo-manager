package my.photomanager.photo.cameraModel;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraModelRepository extends JpaRepository<CameraModel, Long> {

	Optional<CameraModel> findByName(@NonNull String name);
}
