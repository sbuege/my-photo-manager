package my.photoLibrary.core.album;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

	boolean existsByName(String name);

	Optional<Album> findByName(String name);
}
