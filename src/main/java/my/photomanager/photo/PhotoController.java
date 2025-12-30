package my.photomanager.photo;

import java.io.IOException;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.filterOption.FilterOptionService;
import my.photomanager.filterOption.FilterProperties;
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
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/photos"})
@Log4j2
public class PhotoController {

	private final PhotoService photoService;
	private final FilterOptionService filterService;

	@GetMapping("/index")
	protected String index(Model model) {
		var photoIDs = photoService.filterPhotos(FilterProperties.builder()
				.build());

		//model.addAttribute("mainContent", "fragments/gallery :: gallery");
		model.addAttribute("mainTemplate", "fragments/gallery");
		model.addAttribute("mainFragment", "gallery");

		model.addAttribute("activeCameraFilters", List.of());
		model.addAttribute("cameraFilters", filterService.getCameraModelFilters());

		model.addAttribute("activeLocationFilters", List.of());
		model.addAttribute("locationFilters", filterService.getLocationFilters());

		model.addAttribute("activeCreationDateFilters", List.of());
		model.addAttribute("creationDateFilters", filterService.getCreationYearFilters());

		model.addAttribute("activeOrientationFilters", List.of());
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
	protected String filterPhotos(Model model, @RequestParam(required = false) List<Long> cameraIds, @RequestParam(required = false) List<Long> locationIds,
			@RequestParam(required = false) List<Integer> creationYears, @RequestParam(required = false) List<Long> orientationIds) {
		var photoIDs = photoService.filterPhotos(FilterProperties.builder()
				.withCameraModelIds(cameraIds)
				.withLocationIDs(locationIds)
				.withOrientationIDs(orientationIds)
				.build());
		model.addAttribute("activeCameraFilters", cameraIds);
		model.addAttribute("cameraFilters", filterService.getCameraModelFilters());

		model.addAttribute("activeLocationFilters", locationIds);
		model.addAttribute("locationFilters", filterService.getLocationFilters());

		model.addAttribute("activeCreationDateFilters", creationYears);
		model.addAttribute("creationDateFilters", filterService.getCreationYearFilters());

		model.addAttribute("activeOrientationFilters", orientationIds);
		model.addAttribute("orientationFilters", filterService.getOrientationFilters());
		model.addAttribute("photoIDs", photoIDs);

		return "index";
	}
}
