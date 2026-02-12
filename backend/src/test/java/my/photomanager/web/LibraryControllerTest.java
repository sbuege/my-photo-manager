package my.photomanager.web;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Stream;
import my.photomanager.TestDataBuilder;
import my.photomanager.core.library.LibraryService;
import my.photomanager.core.library.LibraryServiceException;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LibraryController.class)
class LibraryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private LibraryService libraryService;

	public static Stream<String> invalidStringParameterProvider() {
		return Stream.of(Strings.EMPTY, " ");
	}

	@Test
	void shouldGetLibraries() throws Exception {
		mockMvc.perform(get("/library/"))
				.andExpect(status().isOk());
	}

	@Nested
	class AddLibraryTest {

		@Test
		void shouldAddLibrary() throws Exception {
			mockMvc.perform(post("/library/add")
							.param("name", TestDataBuilder.TestLibraryName)
							.param("path", TestDataBuilder.TestLibraryPath))
					.andExpect(status().isOk());
		}

		@ParameterizedTest
		@MethodSource("my.photomanager.web.LibraryControllerTest#invalidStringParameterProvider")
		void shouldReturnBadRequestWhenNameIsEmpty(String name) throws Exception {
			mockMvc.perform(post("/library/add")
							.param("name", name)
							.param("path", TestDataBuilder.TestLibraryPath))
					.andExpect(status().isBadRequest());
		}

		@ParameterizedTest
		@MethodSource("my.photomanager.web.LibraryControllerTest#invalidStringParameterProvider")
		void shouldReturnBadRequestWhenPathIsEmpty(String path) throws Exception {
			mockMvc.perform(post("/library/add")
							.param("name", TestDataBuilder.TestLibraryName)
							.param("path", path))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	class EditLibraryTest {

		@Test
		void shouldEditLibrary() throws Exception {
			mockMvc.perform(put("/library/edit/" + TestDataBuilder.TestLibraryId)
							.param("name", "new" + TestDataBuilder.TestLibraryName))
					.andExpect(status().isOk());
		}

		@ParameterizedTest
		@MethodSource("my.photomanager.web.LibraryControllerTest#invalidStringParameterProvider")
		void shouldReturnBadRequestWhenNameIsEmpty(String name) throws Exception {
			mockMvc.perform(put("/library/edit/" + TestDataBuilder.TestLibraryId)
							.param("name", name))
					.andExpect(status().isBadRequest());
		}

		@Test
		void shouldReturnBadRequestWhenLibraryIdNotFound() throws Exception {
			long nonExistingId = TestDataBuilder.TestLibraryId;
			String newLibraryName = "new" + TestDataBuilder.TestLibraryName;

			doThrow(new LibraryServiceException("Library not found"))
					.when(libraryService)
					.editLibrary(nonExistingId,newLibraryName);

			mockMvc.perform(put("/library/edit/" + nonExistingId))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	class DeleteLibraryTest {

		@Test
		void shouldDeleteLibrary() throws Exception {
			mockMvc.perform(delete("/library/delete/" + TestDataBuilder.TestLibraryId))
					.andExpect(status().isOk());
		}

		@Test
		void shouldReturnBadRequestWhenLibraryIdNotFound() throws Exception {
			long nonExistingId = TestDataBuilder.TestLibraryId;

			doThrow(new LibraryServiceException("Library not found"))
					.when(libraryService)
					.deleteLibrary(nonExistingId);

			mockMvc.perform(delete("/library/delete/" + nonExistingId))
					.andExpect(status().isBadRequest());
		}
	}
}