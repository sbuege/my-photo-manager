package my.photomanager.photo.album;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PhotoAlbumService {

	private final PhotoAlbumRepository photoAlbumRepository;

	protected PhotoAlbumService(@NonNull PhotoAlbumRepository photoAlbumRepository) {
		this.photoAlbumRepository = photoAlbumRepository;
	}

	/**
	 * Saves the given {@link PhotoAlbum} if no album with the same name exists,
	 * or returns the existing one.
	 *
	 * @param photoAlbum the {@link PhotoAlbum} to save or retrieve; must not be {@code null}
	 * @return the existing or newly saved {@link PhotoAlbum}
	 */
	public PhotoAlbum saveOrGetPhotoAlbum(@NonNull PhotoAlbum photoAlbum) {
		return photoAlbumRepository.findByName(photoAlbum.getName())
				.orElseGet(() -> {
					var savedPhotoAlum = photoAlbumRepository.saveAndFlush(photoAlbum);
					log.info("saved photo album with name {} successfully", photoAlbum.getName());

					return savedPhotoAlum;
				});
	}

}
