package my.photoLibrary.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import my.photoLibrary.core.library.LibraryService;
import my.photoLibrary.core.photo.PhotoService;
import my.photoLibrary.web.filter.FilterService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PhotoController.class)
class PhotoControllerTest {

	@MockitoBean
	private  PhotoService photoService;

	@MockitoBean
	private  FilterService filterService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnIndexPage() throws Exception {
		mockMvc.perform(get("/photos/index"))
				.andExpect(status().isOk());

	}
}