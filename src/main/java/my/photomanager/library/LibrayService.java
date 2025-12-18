package my.photomanager.library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.photo.PhotoBuilder;
import my.photomanager.photo.PhotoBuilderException;
import my.photomanager.photo.PhotoService;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import my.photomanager.utils.metaDataParser.MetadataParserException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
public class LibrayService {

	private final List<String> PHOTO_EXTENSIONS = List.of("jpg", "jpeg", "webp");

	private final LibraryRepository repository;
	private final PhotoBuilder photoBuilder;
	private final PhotoService photoService;

	/**
	 * Saves the given {@link Library} if no library with the same path exists
	 * or update the existing one.
	 *
	 * @param library the {@link Library} to save or retrieve
	 */
	public Library saveLibray(@NonNull Library library) {
		return repository.findByPath(library.getPath())
				.orElseGet(() -> {
					var savedPhotoAlum = repository.saveAndFlush(library);
					log.info("saved library with path {} successfully", library.getPath());

					return savedPhotoAlum;
				});
	}

	public void indexLibrary(@NonNull Library library) {

		log.info("indexing photos from {}", library.getPath());

		try {
			Files.walk(Path.of(library.getPath()))
					.filter(Files::isRegularFile)
					.filter(this::isPhotoFile)
					.forEach((path) -> {
						try {
							if (!photoService.existsPhotoByHashValue(path)) {
								var photo = photoBuilder.buildPhoto(path);
								photoService.saveIfPhotoNotExists(photo);
							}
						} catch (MetadataParserException | GpsResolverException | IOException | PhotoBuilderException e) {
							throw new RuntimeException(e);
						}
					});
		} catch (IOException e) {
			log.error("an error occurred while indexing photos", e);
			throw new RuntimeException(e);
		}
	}

	private boolean isPhotoFile(@NonNull Path path) {
		String fileName = path.getFileName()
				.toString()
				.toLowerCase();
		return PHOTO_EXTENSIONS.stream()
				.anyMatch(fileName::endsWith);
	}

}
