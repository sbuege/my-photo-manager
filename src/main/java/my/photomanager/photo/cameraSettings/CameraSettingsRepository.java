package my.photomanager.photo.cameraSettings;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CameraSettingsRepository extends JpaRepository<CameraSettings, Integer> {

	Optional<CameraSettings> findByCameraModelName(@NonNull String cameraModelName);
}
