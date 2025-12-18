package my.photomanager.library;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		return "index";
	}

	@PostMapping("/add")
	protected String addLibrary(@RequestParam(required = true, name = "folderPath") String folderPath) {
		var library = libraryService.saveLibray(Library.builder()
				.withName("Test Library")
				.withPath(folderPath)
				.build());

		libraryService.indexLibrary(library);

		return "redirect:/";
	}
}
