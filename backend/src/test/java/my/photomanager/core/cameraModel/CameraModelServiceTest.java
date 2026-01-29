package my.photomanager.core.cameraModel;

import static my.photomanager.TestDataBuilder.TestAlbumId;
import static my.photomanager.TestDataBuilder.TestCameraModelId;
import static my.photomanager.TestDataBuilder.TestCameraModelName;
import static my.photomanager.TestDataBuilder.buildCameraModel;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;
import my.photomanager.utils.metaDataParser.Metadata;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CameraModelServiceTest {

	@Mock
	private CameraModelRepository repository;

	@InjectMocks
	private CameraModelService service;

	@Test
	void shouldReturnAllCameraModels() {
		// --- WHEN
		service.getAllCameraModels();

		// --- THEN
		verify(repository).findAll();
	}

	@Nested
	class CreateCameraModelTest {

		@Test
		void shouldCreateAndSaveCameraModel() {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(false);

			// --- WHEN ---
			service.createAndSaveCameraModel(TestCameraModelName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(CameraModel.class));
		}

		public static Stream<Metadata> metaDataProvider() {
			return Stream.of(
					new Metadata(0, 0, TestCameraModelName, null, 0.0d, 0.0d),
					new Metadata(0, 0, Strings.EMPTY, null, 0.0d, 0.0d),
					new Metadata(0, 0, "", null, 0.0d, 0.0d)
			);
		}

		@ParameterizedTest
		@MethodSource("metaDataProvider")
		void shouldCreateAndSaveCameraModelFromMetaData(Metadata metaData) {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(false);

			// --- WHEN ---
			service.createAndSaveCameraModel(metaData);

			// --- THEN ---
			verify(repository).saveAndFlush(any(CameraModel.class));
		}

		@Test
		void shouldReturnExistingCameraModel() {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(true);
			when(repository.findByName(anyString())).thenReturn(Optional.of(buildCameraModel()));

			// --- WHEN ---
			service.createAndSaveCameraModel(TestCameraModelName);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(CameraModel.class));
		}
	}

	@Nested
	class EditCameraModelTest {

		private final String newCameraModelName = "newCameraModelName";

		@Test
		void shouldEditAndSaveCameraModel() {
			// --- GIVEN ---
			when(repository.findById(TestCameraModelId)).thenReturn(Optional.of(buildCameraModel()));
			when(repository.existsByName(newCameraModelName)).thenReturn(false);

			// --- WHEN ---
			service.editCameraModel(TestAlbumId, newCameraModelName);

			// --- THEN ---
			verify(repository).saveAndFlush(any(CameraModel.class));
		}

		@Test
		void shouldReturnExistingCameraModel() {
			// --- GIVEN ---
			when(repository.findById(TestCameraModelId)).thenReturn(Optional.of(buildCameraModel()));
			when(repository.existsByName(newCameraModelName)).thenReturn(true);
			when(repository.findByName(newCameraModelName)).thenReturn(Optional.of(buildCameraModel(newCameraModelName)));

			// --- WHEN ---
			service.editCameraModel(TestCameraModelId, newCameraModelName);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(CameraModel.class));
		}

		@Test
		void shouldThrowExceptionWhenCameraModelDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestCameraModelId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(CameraModelServiceException.class, () -> service.editCameraModel(TestCameraModelId, newCameraModelName));
			verify(repository, never()).saveAndFlush(any(CameraModel.class));
		}
	}

	@Nested
	class DeleteCameraModelTest {

		@Test
		void shouldDeleteCameraModel() {
			// --- GIVEN ---
			when(repository.findById(TestCameraModelId)).thenReturn(Optional.of(buildCameraModel()));

			// --- WHEN ---
			service.deleteCameraModel(TestCameraModelId);

			// --- THEN ---
			verify(repository).deleteById(TestCameraModelId);
		}

		@Test
		void shouldThrowExceptionWhenCameraModelDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(TestCameraModelId)).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(CameraModelServiceException.class, () -> service.deleteCameraModel(TestCameraModelId));
			verify(repository, never()).deleteById(TestCameraModelId);
		}
	}
}