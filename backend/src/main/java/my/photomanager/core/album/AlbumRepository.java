package my.photomanager.core.album;

import java.util.Optional;
import my.photomanager.core.album.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

	boolean existsByName(String name);

	Optional<Album> findByName(String name);
}
