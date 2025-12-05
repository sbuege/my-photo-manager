package my.photomanager.library;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {

	Optional<Library> findByPath(String path);
	
}
