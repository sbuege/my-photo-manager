package my.photomanager;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/"})
public class PageController {

	@GetMapping
	protected String index(Model model) {
		return "forward:/photos/index";
	}

	@GetMapping("/library")
	protected String library(Model model) {
		return "forward:/library/index";
	}

}
