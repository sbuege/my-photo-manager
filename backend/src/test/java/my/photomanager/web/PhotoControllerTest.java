package my.photomanager.web;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import my.photomanager.TestDataBuilder;
import my.photomanager.core.photo.PhotoService;
import my.photomanager.core.photo.PhotoServiceException;
import my.photomanager.web.filter.FilterService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PhotoController.class)
class PhotoControllerTest {

	@MockitoBean
	private PhotoService photoService;

	@MockitoBean
	private FilterService filterService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void shouldGetPhotos() throws Exception {
		mockMvc.perform(get("/photos/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.photoIDs", hasSize(0)))
				.andExpect(jsonPath("$.activeCameraModelIDs", hasSize(0)))
				.andExpect(jsonPath("$.cameraFilters").exists())
				.andExpect(jsonPath("$.activeLocationIDs", hasSize(0)))
				.andExpect(jsonPath("$.locationFilters").exists())
				.andExpect(jsonPath("$.activeCreationDateFilters", hasSize(0)))
				.andExpect(jsonPath("$.creationDateFilters").exists())
				.andExpect(jsonPath("$.activeOrientationIDs", hasSize(0)))
				.andExpect(jsonPath("$.orientationFilters").exists());
	}

	@Nested
	class ThumbnailTest {

		@Test
		void shouldReturnThumbnail() throws Exception {
			when(photoService.findById(TestDataBuilder.TestPhotoId)).thenReturn(TestDataBuilder.buildPhoto001());

			mockMvc.perform(get("/photos/thumbnail/" + TestDataBuilder.TestPhotoId))
					.andExpect(status().isOk());
		}

		@Test
		void shouldReturnBadRequestWhenPhotoIDDoesNotExist() throws Exception {
			long nonExistingId = TestDataBuilder.TestPhotoId;

			doThrow(new PhotoServiceException("Photo not found"))
					.when(photoService)
					.findById(nonExistingId);

			mockMvc.perform(get("/photos/thumbnail/" + nonExistingId))
					.andExpect(status().isBadRequest());
		}
	}
}