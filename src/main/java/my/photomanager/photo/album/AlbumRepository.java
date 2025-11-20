package my.photomanager.photo.album;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {

	Optional<Album> findByName(@NonNull String name);
}
