package my.photomanager.photo.category;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoCategoryRepository extends JpaRepository<PhotoCategory, Integer> {

	Optional<PhotoCategory> findByName(@NonNull String name);
}
