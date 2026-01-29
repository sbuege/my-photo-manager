package my.photomanager.core.library;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.photo.PhotoService;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import my.photomanager.utils.metaDataParser.MetadataParserException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class LibraryService {

	private final List<String> PHOTO_EXTENSIONS = List.of("jpg", "jpeg", "webp");

	private final LibraryRepository repository;
	private final PhotoService photoService;

	/**
	 * Indexes a library by scanning its directory for photo files and updating its metadata,
	 * such as the number of photos and the timestamp of the last indexing operation.
	 *
	 * @param ID the unique identifier of the library to be indexed
	 * @throws IOException if an error occurs while accessing the file system
	 */
	public Library indexLibrary(long ID) throws IOException {

		Predicate<Path> isPhotoFile = (photoPath) -> {
			String fileName = photoPath.getFileName()
					.toString()
					.toLowerCase();
			return PHOTO_EXTENSIONS.stream()
					.anyMatch(fileName::endsWith);
		};

		var library = repository.findById(ID)
				.orElseThrow(() -> new LibraryServiceException("no library found with id " + ID));

		var photos = Files.walk(Path.of(library.getPath()))
				.filter(Files::isRegularFile)
				.filter(isPhotoFile)
				.toList();

		AtomicInteger numberOfPhotos = new AtomicInteger();
		photos.forEach(photoPath -> {
			try {
				photoService.createAndSavePhoto(photoPath);
			} catch (IOException | MetadataParserException | GpsResolverException e) {
				throw new RuntimeException(e);
			}
			numberOfPhotos.getAndIncrement();
		});

		library.setLastIndexAt(Instant.now());
		library.setNumberOfPhotos(numberOfPhotos.get());

		repository.saveAndFlush(library);
		log.info("indexed library {} successfully", library);

		return library;
	}

	/**
	 * Retrieves a list of all libraries from the repository.
	 *
	 * @return a list containing all libraries
	 */
	public List<Library> getAllLibraries() {
		return repository.findAll();
	}

	/**
	 * Creates a new library with the specified name and path, and saves it to the database.
	 * If a library with the same path already exists, it retrieves and returns that existing library instead.
	 *
	 * @param name the name of the library to be created, must not be null
	 * @param path the unique path of the library to be created, must not be null
	 * @return the saved or retrieved {@link Library} instance
	 */
	public Library createAndSaveLibrary(@NonNull String name, @NonNull String path) {
		var library = new Library(name, path);
		log.debug("created new library {}", library);

		return saveOrGetLibrary(library);
	}

	/**
	 * Edits the name of an existing library identified by its unique ID.
	 * If the library with the given ID is not found, a {@link LibraryServiceException} is thrown.
	 *
	 * @param id   the unique identifier of the library to edit
	 * @param name the new name to assign to the library, must not be null
	 * @return the updated {@link Library} instance
	 */
	public Library editLibrary(long id, @NonNull String name) {
		var library = repository.findById(id)
				.orElseThrow(() -> new LibraryServiceException("no library found with id " + id));
		log.debug("found library {} to edit", library);

		library.setName(name);
		// TODO update path -> reset last index !
		log.info("updated library {} successfully", library);

		return saveOrGetLibrary(library);
	}

	/**
	 * Deletes a library identified by its unique ID.
	 * Throws a {@link LibraryServiceException} if no library with the provided ID is found.
	 *
	 * @param id the unique identifier of the library to be deleted
	 */
	public void deleteLibrary(long id) {
		var library = repository.findById(id)
				.orElseThrow(() -> new LibraryServiceException("no library found with id " + id));
		log.debug("found library {} to delete", library);

		repository.deleteById(id);
		log.info("deleted library {} successfully", library);
	}

	private Library saveOrGetLibrary(@NonNull Library library) {
		Library savedLibrary;

		if (repository.existsByPath(library.getPath())) {
			savedLibrary = repository.findByPath(library.getPath())
					.orElseThrow(() -> new LibraryServiceException("no library found with path " + library.getPath()));
			log.debug("library {} already exists", savedLibrary);
		} else {
			savedLibrary = repository.saveAndFlush(library);
			log.info("saved library {} successfully", savedLibrary);
		}

		return savedLibrary;
	}
}
