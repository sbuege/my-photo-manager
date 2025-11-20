package my.photomanager.photo.album;

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
