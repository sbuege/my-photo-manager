package my.photomanager.photo.album;

import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoAlbumRepository extends JpaRepository<PhotoAlbum, Integer> {

	Optional<PhotoAlbum> findByName(@NonNull String name);
}
