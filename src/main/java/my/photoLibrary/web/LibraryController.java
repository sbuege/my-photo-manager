package my.photoLibrary.web;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photoLibrary.core.library.LibraryService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/library"})
@Log4j2
public class LibraryController {

	private final LibraryService libraryService;

	@PostMapping("/add")
	protected String addLibrary(@RequestParam String name, @RequestParam String path) {
		libraryService.createAndSaveLibrary(name, path);

		return Strings.EMPTY;
	}

	@DeleteMapping("/delete/{ID}")
	protected String deleteLibrary(@PathVariable long ID) {
		libraryService.deleteLibrary(ID);

		return Strings.EMPTY;
	}
}
