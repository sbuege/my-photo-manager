package my.photomanager.core.album;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     * Creates a new album with the specified name and saves it in the repository if it does not already exist.
     * If an album with the given name already exists, retrieves the existing album instead.
     * If the provided name is blank, logs a warning and returns an empty {@code Optional}.
     *
     * @param name the name of the album to be created, must not be null or blank
     * @return an {@code Optional} containing the created or existing album, or an empty {@code Optional} if the name is blank
     */
    public Optional<Album> createAndSaveAlbum(@NonNull String name) {
        if (name.isBlank()) {
            log.warn("album name cannot be blank");
            return Optional.empty();
        }

        log.debug("creating new album with name {}", name);
        var album = new Album(name);
        log.debug("created new album {}", album);

        return Optional.of(saveOrGetAlbum(album));
    }


    /**
     * Updates the name of an existing album identified by its unique identifier.
     * If the album does not exist, an {@code AlbumServiceException} is thrown.
     * A blank name is logged as a warning and returns an empty {@code Optional}.
     *
     * @param id the unique identifier of the album to be edited
     * @param name the new name to update the album with, must not be null or blank
     * @return an {@code Optional} containing the updated album, or an empty {@code Optional} if the provided name is blank
     */
    public Optional<Album> editAlbum(long id, @NonNull String name) {
        if (name.isBlank()) {
            log.warn("album name cannot be blank");
            return Optional.empty();
        }

        var album = repository.findById(id)
                .orElseThrow(() -> new AlbumServiceException("no album found with id " + id));
        log.debug("found album {} to edit", album);

        log.debug("updating album name to {}", name);
        album.setName(name);
        log.info("updated album {} successfully", album);

        return Optional.ofNullable(saveOrGetAlbum(album));
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

        log.debug("deleting album {}", album);
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
