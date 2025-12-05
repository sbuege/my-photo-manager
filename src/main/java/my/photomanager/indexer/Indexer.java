package my.photomanager.indexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import my.photomanager.config.PhotoManagerConfiguration;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import my.photomanager.metadata.MetadataParserException;
import my.photomanager.photo.PhotoBuilder;
import my.photomanager.photo.PhotoBuilderException;
import my.photomanager.photo.PhotoService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Indexer {

	private final PhotoManagerConfiguration configuration;
	private final PhotoBuilder photoBuilder;
	private final PhotoService photoService;

	private final List<String> PHOTO_EXTENSIONS = List.of("jpg", "jpeg", "webp");

	protected Indexer(PhotoManagerConfiguration configuration, PhotoBuilder photoBuilder, PhotoService photoService) {
		this.configuration = configuration;
		this.photoBuilder = photoBuilder;
		this.photoService = photoService;
	}

	@Async
	public void indexPhotos() {
		var photoSourceFolder = Path.of(configuration.getPhotoSourceFolder());
		log.info("indexing photos from {}", photoSourceFolder);

		try {
			Files.walk(photoSourceFolder)
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
