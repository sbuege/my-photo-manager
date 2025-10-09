package my.photomanager.photo;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

	Optional<Photo> findByHashValue(@NonNull String hashValue);

	boolean existsByHashValue(@NonNull String hashValue);
}
