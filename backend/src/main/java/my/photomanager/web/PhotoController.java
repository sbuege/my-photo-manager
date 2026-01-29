package my.photomanager.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.photo.PhotoService;
import my.photomanager.core.photo.PhotoServiceException;
import my.photomanager.web.filter.ActiveFilter;
import my.photomanager.web.filter.FilterService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/photos"})
@Log4j2
public class PhotoController {

	private final PhotoService photoService;
	private final FilterService filterService;


	@ExceptionHandler(PhotoServiceException.class)
	public ResponseEntity<String> handlePhotoServiceExceptionException(PhotoServiceException e) {
		return ResponseEntity.badRequest()
				.body(e.getMessage());
	}

	@GetMapping("/test")
	protected ResponseEntity<List<Long>> getTestIds() {
		return ResponseEntity.ok(List.of(1L, 2L, 3L));
	}

	@GetMapping("/")
	protected ResponseEntity<Map<String, List<?>>> getPhotos() {
		var emptyActiveFilter = new ActiveFilter(List.of(), List.of(), List.of(), List.of());
		var photoIDs = filterService.filterPhotos(emptyActiveFilter);

		var data = Map.of(
				"activeCameraModelIDs", List.of(),
				"cameraFilters", filterService.getCameraModelStatistics(),
				"activeLocationIDs", List.of(),
				"locationFilters", filterService.getLocationStatistics(),
				"activeCreationDateFilters", List.of(),
				"creationDateFilters", filterService.getCreationYearStatistics(),
				"activeOrientationIDs", List.of(),
				"orientationFilters", filterService.getOrientationStatistics(),
				"photoIDs", photoIDs
		);
		return ResponseEntity.ok(data);
	}

	@GetMapping("/thumbnail/{ID}")
	protected ResponseEntity<byte[]> getPhotoThumbnail(@PathVariable long ID) throws IOException {
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(getThumbnail(ID));
	}

	private byte[] getThumbnail(@NonNull Long ID) throws IOException {
		var photo = photoService.findById(ID);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Thumbnails.of(Path.of(photo.getFileName())
						.toFile())
				.scale(0.25)
				.toOutputStream(out);

		Base64.getEncoder()
				.encodeToString(out.toByteArray());

		return out.toByteArray();
	}
}
