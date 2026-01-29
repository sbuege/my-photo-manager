package my.photoLibrary.core.album;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class AlbumService {

	private final AlbumRepository repository;

	/**
	 * Retrieves a list of all albums stored in the repository.
	 *
	 * @return a list of {@code Album} objects representing all albums in the repository, or an empty list if no albums are present.
	 */
	public List<Album> getAllAlbums() {
		return repository.findAll();
	}

	/**
	 * Creates a new album with the specified name, logs its creation,
	 * and saves it to the repository if it does not already exist.
	 * If an album with the same name already exists, it is retrieved from the repository.
	 *
	 * @param name the name of the album to be created and saved, must not be null
	 * @return the created and saved album, or the existing album if one already exists with the given name
	 */
	public Album createAndSaveAlbum(@NonNull String name) {
		var album = new Album(name);
		log.debug("created new album {}", album);

		return saveOrGetAlbum(album);
	}

	/**
	 * Updates the name of the album with the specified ID. If the album is not found,
	 * an {@code AlbumServiceException} is thrown. The method retrieves the existing
	 * album, updates its name, and either saves the updated album or retrieves an
	 * existing one if it already exists with the new name.
	 *
	 * @param id the unique identifier of the album to be edited
	 * @param name the new name to assign to the album, must not be null
	 * @return the updated and saved {@code Album} object
	 */
	public Album editAlbum(long id, @NonNull String name) {
		var album = repository.findById(id)
				.orElseThrow(() -> new AlbumServiceException("no album found with id " + id));
		log.debug("found album {} to edit", album);

		album.setName(name);
		log.info("updated album {} successfully", album);

		return saveOrGetAlbum(album);
	}

	/**
	 * Deletes an album with the specified ID from the repository.
	 * If the album does not exist, an {@code AlbumserviceException} is thrown.
	 *
	 * @param id the unique identifier of the album to be deleted
	 */
	public void deleteAlbum(long id) {
		var album = repository.findById(id)
				.orElseThrow(() -> new AlbumServiceException("no album found with id " + id));
		log.debug("found album {} to delete", album);

		repository.deleteById(id);
		log.info("deleted album {} successfully", album);
	}

	private Album saveOrGetAlbum(@NonNull Album album) {
		Album savedAlbum;

		if (repository.existsByName(album.getName())) {
			savedAlbum = repository.findByName(album.getName())
					.orElseThrow(() -> new AlbumServiceException("no album found with name " + album.getName()));
			log.debug("album {} already exists", savedAlbum);
		} else {
			savedAlbum = repository.saveAndFlush(album);
			log.info("saved album {} successfully", savedAlbum);
		}

		return savedAlbum;
	}
}
