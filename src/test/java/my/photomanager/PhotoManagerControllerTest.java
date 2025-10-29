package my.photomanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(PhotoManagerController.class)
@Import({TestUtils.PhotoIndexerMock.class, TestUtils.PhotoServiceMock.class, TestUtils.FilterServiceMock.class})
class PhotoManagerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldReturnIndexPage() throws Exception {
		mockMvc.perform(get("/photos"))
				.andExpect(status().isOk());
	}
}