package my.photomanager;

import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import my.photomanager.filter.FilterProperties;
import my.photomanager.photo.PhotoService;
import my.photomanager.photo.PhotoServiceException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/photos",})
@Log4j2
public class PhotoController {

	private final PhotoService photoService;

	protected PhotoController(PhotoService photoService) {
		this.photoService = photoService;
	}

	@GetMapping
	protected String index(Model model) {
		log.info("get homepage");

		var photoIDs = photoService.filterPhotos(FilterProperties.builder()
				.build());

		model.addAttribute("photoIDs", photoIDs);


		return "index";
	}

	@GetMapping("/{id}/thumbnail")
	protected ResponseEntity<byte[]> getPhotoById(@PathVariable long id) throws PhotoServiceException, IOException {
		log.info("get photo thumbnail by id {}", id);

		return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.body(photoService.getThumbnail(id));
	}
}
