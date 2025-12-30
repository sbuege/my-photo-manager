package my.photomanager.library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

	protected List<LibraryDto> getAllLibraries() {
		return repository.findAll()
				.stream()
				.map(this::toDTO)
				.toList();
	}

	/**
	 * Adds a new library to the repository if no library with the same path already exists.
	 *
	 * @param name the descriptive name of the library
	 * @param path the file system path where the library is located
	 */
	protected void addLibrary(@NonNull String name, @NonNull String path) {
		var library = new Library(name, path);
		log.info("created new library {}", library);

		saveLibray(library);
	}

	/**
	 * Updates the details of an existing library.
	 *
	 * @param ID   the unique identifier of the library to edit
	 * @param name the new name to assign to the library
	 * @param path the new file system path to assign to the library
	 * @throws RuntimeException if no library is found with the given ID
	 */
	protected void editLibrary(long ID, String name, String path) {
		var library = repository.findById(ID)
				.orElseThrow(() -> new RuntimeException("no library found with id " + ID));

		library.setName(name);
		library.setPath(path);

		repository.saveAndFlush(library);
	}

	/**
	 * Deletes a library from the repository.
	 *
	 * @param ID the unique identifier of the library to delete
	 * @throws RuntimeException if no library is found with the given ID
	 */
	protected void deleteLibrary(long ID) {
		var library = repository.findById(ID)
				.orElseThrow(() -> new RuntimeException("no library found with id " + ID));

		repository.delete(library);
	}

	/**
	 * Recursively scans the library's path for photo files and indexes them.
	 * For each new photo found (based on its hash value), metadata is parsed,
	 * and the photo entity is persisted in the repository.
	 *
	 * @param library the {@link Library} to be indexed; must not be {@code null}
	 * @throws RuntimeException if an I/O error occurs or metadata processing fails
	 */
	public void indexLibrary(long ID) {
		var library = repository.findById(ID)
				.orElseThrow(() -> new RuntimeException("no library found with id " + ID));

		log.info("indexing photos from {}", library.getPath());
		AtomicInteger numberOfPhotos = new AtomicInteger();
		try {
			Files.walk(Path.of(library.getPath()))
					.filter(Files::isRegularFile)
					.filter(this::isPhotoFile)
					.forEach((path) -> {
						try {
							if (!photoService.existsPhotoByHashValue(path)) {
								var photo = photoBuilder.buildPhoto(path);
								photoService.saveIfPhotoNotExists(photo);
								numberOfPhotos.getAndIncrement();
							}
						} catch (MetadataParserException | GpsResolverException | IOException | PhotoBuilderException e) {
							throw new RuntimeException(e);
						}
					});

			library.setLastScan(Instant.now());
			library.setNumberOfPhotos(numberOfPhotos.get());
			repository.saveAndFlush(library);
		} catch (IOException e) {
			log.error("an error occurred while indexing photos", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Checks if the given file path points to a supported photo format.
	 *
	 * @param path the file path to check; must not be {@code null}
	 * @return {@code true} if the file extension matches one of the supported {@code PHOTO_EXTENSIONS},
	 *        {@code false} otherwise
	 */
	private boolean isPhotoFile(@NonNull Path path) {
		String fileName = path.getFileName()
				.toString()
				.toLowerCase();
		return PHOTO_EXTENSIONS.stream()
				.anyMatch(fileName::endsWith);
	}

	private LibraryDto toDTO(Library library) {
		var lastScan = library.getLastScan();
		var lastScanText = "-";

		if (lastScan != null) {
			var formatter =
					DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
							.withZone(ZoneId.of("Europe/Berlin"));

			lastScanText = formatter.format(lastScan);
		}

		return new LibraryDto(library.getId(), library.getName(), lastScanText, library.getNumberOfPhotos());
	}

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
}
