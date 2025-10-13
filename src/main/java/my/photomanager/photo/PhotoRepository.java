package my.photomanager.photo;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

	Optional<Photo> findByHashValue(@NonNull String hashValue);

	boolean existsByHashValue(@NonNull String hashValue);
}
