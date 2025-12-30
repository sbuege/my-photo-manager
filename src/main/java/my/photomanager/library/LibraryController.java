package my.photomanager.library;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/library"})
@Log4j2
public class LibraryController {

	private final LibrayService libraryService;

	@GetMapping("/index")
	protected String index(Model model) {

		//model.addAttribute("mainContent", "fragments/library :: library");
		model.addAttribute("mainTemplate", "fragments/library");
		model.addAttribute("mainFragment", "library");

		model.addAttribute("libraries", libraryService.getAllLibraries());

		return "index";
	}

	@PostMapping("/add")
	protected String addLibrary(@RequestParam String name, @RequestParam String path) {

		libraryService.addLibrary(name, path);
		//libraryService.indexLibrary(library);

		return "redirect:/library/index";
	}

	@GetMapping("/index/{ID}")
	protected String indexLibrary(@PathVariable long ID) {
		libraryService.indexLibrary(ID);
		return "redirect:/";
	}



	@PutMapping("/edit/{ID}")
	protected void editLibrary(@PathVariable long ID, @RequestParam String newLibraryName, @RequestParam String newFolderPath) {
		log.info("edit library with id {} to name {} and path {}", ID, newLibraryName, newFolderPath);

		// TODO
	}

	@DeleteMapping("/delete/{ID}")
	protected void deleteLibrary(@PathVariable long ID) {
		log.info("delete library with id {}", ID);

		// TODO
	}
}
