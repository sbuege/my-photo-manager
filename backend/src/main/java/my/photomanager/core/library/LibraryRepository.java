package my.photomanager.core.library;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {

	boolean existsByPath(String path);

	Optional<Library> findByPath(String path);
}
