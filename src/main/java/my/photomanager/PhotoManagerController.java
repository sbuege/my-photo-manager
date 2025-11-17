package my.photomanager;

import java.io.IOException;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import my.photomanager.filterOption.FilterProperties;
import my.photomanager.filterOption.FilterOptionService;
import my.photomanager.photo.PhotoService;
import my.photomanager.photo.PhotoServiceException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({"/photos",})
@Log4j2
public class PhotoManagerController {

	private final PhotoService photoService;
	private final FilterOptionService filterService;

	protected PhotoManagerController(PhotoService photoService, FilterOptionService filterService) {
		this.photoService = photoService;
		this.filterService = filterService;
	}

	@GetMapping
	protected String index(Model model) {
		log.info("get homepage");

		var photoIDs = photoService.filterPhotos(FilterProperties.builder()
				.build());

		model.addAttribute("cameraFilters", filterService.getCameraSettingsFilters());
		model.addAttribute("locationFilters", filterService.getLocationFilters());
		model.addAttribute("creationDateFilters", filterService.getCreationDateFilters());
		model.addAttribute("orientationFilters", filterService.getOrientationFilters());
		model.addAttribute("photoIDs", photoIDs);

		return "index";
	}

	@GetMapping("/{id}/thumbnail")
	protected ResponseEntity<byte[]> getPhotoThumbnail(@PathVariable long id) throws PhotoServiceException, IOException {
		log.info("get photo thumbnail by id {}", id);

		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(photoService.getThumbnail(id));
	}

	@PostMapping("/filter")
	protected String filterPhotos(Model model, @RequestParam(required = false) List<Long> cameraIds, @RequestParam(required = false) List<Long> locationIds) {
		log.info("filter photos");
		log.debug("camera ids: {}", cameraIds);
		log.debug("location ids: {}", locationIds);

		var photoIDs = photoService.filterPhotos(FilterProperties.builder()
				.withCameraModelIds(cameraIds)
				.withLocationIDs(locationIds)
				.build());

		model.addAttribute("cameraFilters", filterService.getCameraSettingsFilters());
		model.addAttribute("locationFilters", filterService.getLocationFilters());
		model.addAttribute("creationDateFilters", filterService.getCreationDateFilters());
		model.addAttribute("orientationFilters", filterService.getOrientationFilters());
		model.addAttribute("photoIDs", photoIDs);

		return "index";
	}
}
