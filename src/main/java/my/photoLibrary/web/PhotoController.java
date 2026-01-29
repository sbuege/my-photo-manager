package my.photoLibrary.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photoLibrary.core.photo.PhotoService;
import my.photoLibrary.web.filter.ActiveFilter;
import my.photoLibrary.web.filter.FilterService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/photos"})
@Log4j2
public class PhotoController {

	private final PhotoService photoService;
	private final FilterService filterService;

	@GetMapping("/index")
	protected String index(Model model) {
		var emptyActiveFilter = new ActiveFilter(List.of(), List.of(), List.of(), List.of());
		var photoIDs = filterService.filterPhotos(emptyActiveFilter);

		addModelAttributes(model, List.of(), List.of(), List.of(), photoIDs);

		return Strings.EMPTY;
	}

	@GetMapping("/{id}/thumbnail")
	protected ResponseEntity<byte[]> getPhotoThumbnail(@PathVariable long id) throws IOException {
		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(getThumbnail(id));
	}

	private byte[] getThumbnail(@NonNull Long id) throws IOException {
		var photo = photoService.findById(id);
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Thumbnails.of(Path.of(photo.getFileName())
						.toFile())
				.scale(0.25)
				.toOutputStream(out);

		Base64.getEncoder()
				.encodeToString(out.toByteArray());

		return out.toByteArray();
	}

	private void addModelAttributes(Model model, List<Long> activeCameraModelIDs, List<Long> activeLocationIDs, List<Long> activeOrientationIDs,
			List<Long> photoIDs) {
		model.addAttribute("activeCameraModelIDs", activeCameraModelIDs);
		model.addAttribute("cameraFilters", filterService.getCameraModelStatistics());

		model.addAttribute("activeLocationIDs", activeLocationIDs);
		model.addAttribute("locationFilters", filterService.getLocationStatistics());

		model.addAttribute("activeCreationDateFilters", List.of());
		model.addAttribute("creationDateFilters", filterService.getCreationYearStatistics());

		model.addAttribute("activeOrientationIDs", activeOrientationIDs);
		model.addAttribute("orientationFilters", filterService.getOrientationStatistics());

		model.addAttribute("photoIDs", photoIDs);
	}
}
