package my.photomanager.photo;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import my.photomanager.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PhotoController.class)
@TestInstance(PER_CLASS)
@Import({TestUtils.PhotoIndexerMock.class, TestUtils.PhotoServiceMock.class, TestUtils.FilterServiceMock.class})
class PhotoControllerTest {


	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnIndexPage() throws Exception {
		mockMvc.perform(get("/photos"))
				.andExpect(status().isOk())
				.andExpect(view().name("index"))
				.andExpect(model().attributeExists("photoIDs"));
	}
}