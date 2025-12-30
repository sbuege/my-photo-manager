package my.photomanager.photo.album;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class AlbumService {

	private final AlbumRepository repository;

	protected void addAlbum(@NonNull String name) {
		var album = new Album(name);
		log.info("created new album {}", album);

		saveAlbumIfNotExists(album);
	}

	protected void updateAlbum(long id, @NonNull String name) {
		var album = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("no album found with id " + id));
		log.info("found album {} to edit", album);

		album.setName(name);
		log.info("updated album {} successfully", album);

		// TODO what happen if album with same name exists already
		// TODO update all photos ???
	}

	protected void deleteAlbum(long id) {
		var album = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("no album found with id " + id));
		log.info("found album {} to delete", album);

		repository.delete(album);
		log.info("deleted album {} successfully", album);
		// TODO delete album
		// TODO update all photos ???
	}

	private Album saveAlbumIfNotExists(@NonNull Album album) {
		return null;
	}

	/**
	 * Saves the given {@link Album} if no album with the same name exists,
	 * or returns the existing one.
	 *
	 * @param album the {@link Album} to save or retrieve
	 * @return the existing or newly saved {@link Album}
	 */
	public Album saveOrGetAlbum(@NonNull Album album) {
		return repository.findByName(album.getName())
				.orElseGet(() -> {
					var savedPhotoAlum = repository.saveAndFlush(album);
					log.info("saved album with name {} successfully", album.getName());

					return savedPhotoAlum;
				});
	}
}
