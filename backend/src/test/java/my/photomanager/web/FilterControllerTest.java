package my.photomanager.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import my.photomanager.web.filter.FilterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FilterController.class)
public class FilterControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private FilterService filterService;

	@Test
	void shouldGetCameraFilter() throws Exception {
		mockMvc.perform(get("/filter/cameraFilter"))
				.andExpect(status().isOk());
	}

	@Test
	void shouldGetLocationFilter() throws Exception {
		mockMvc.perform(get("/filter/locationFilter"))
				.andExpect(status().isOk());
	}

	@Test
	void shouldGetOrientationFilter() throws Exception {
		mockMvc.perform(get("/filter/orientationFilter"))
				.andExpect(status().isOk());
	}


}
