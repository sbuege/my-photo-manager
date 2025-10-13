package my.photomanager.photo.album;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoAlbumRepository extends JpaRepository<PhotoAlbum, Long> {

	Optional<PhotoAlbum> findByName(@NonNull String name);
}
