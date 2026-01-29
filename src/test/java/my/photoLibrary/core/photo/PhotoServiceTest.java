package my.photoLibrary.core.photo;

import static my.photoLibrary.TestDataBuilder.buildCameraModel;
import static my.photoLibrary.TestDataBuilder.buildLocation;
import static my.photoLibrary.TestDataBuilder.buildOrientation;
import static my.photoLibrary.TestDataBuilder.buildPhoto;
import static my.photoLibrary.TestDataBuilder.createTestPhotoFile;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import my.photoLibrary.TestDataBuilder;
import my.photoLibrary.core.cameraModel.CameraModelService;
import my.photoLibrary.core.location.LocationService;
import my.photoLibrary.core.orientation.OrientationService;
import my.photoLibrary.utils.gpsResolver.GpsResolverException;
import my.photoLibrary.utils.metaDataParser.Metadata;
import my.photoLibrary.utils.metaDataParser.MetadataParser;
import my.photoLibrary.utils.metaDataParser.MetadataParserException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhotoServiceTest {

	@Mock
	private PhotoRepository repository;

	@Mock
	private LocationService locationService;

	@Mock
	private CameraModelService cameraModelService;

	@Mock
	private OrientationService orientationService;

	@InjectMocks
	private PhotoService service;

	@Nested
	class FindPhotoTest {

		@Test
		void shouldFindPhoto() {
			// --- GIVEN ---
			when(repository.findById(anyLong())).thenReturn(Optional.of(buildPhoto()));

			// --- WHEN ---
			service.findById(TestDataBuilder.TestPhotoId);

			// --- THEN ---
			verify(repository).findById(anyLong());
		}

		@Test
		void shouldThrowExceptionWhenPhotoDoesNotExist() {
			// --- GIVEN ---
			when(repository.findById(anyLong())).thenReturn(Optional.empty());

			// --- WHEN / THEN ---
			assertThrows(PhotoServiceException.class, () -> service.findById(TestDataBuilder.TestPhotoId));
		}
	}

	@Nested
	class CreatePhotoTest {

		@Test
		void shouldCreateAndSavePhoto() throws MetadataParserException, IOException, GpsResolverException {
			try (var metaDataParser = mockStatic(MetadataParser.class)) {
				// --- GIVEN ---
				metaDataParser.when(() -> MetadataParser.parseMetadata(any(Path.class)))
						.thenReturn(TestDataBuilder.buildMetadata());

				when(repository.existsByHashValue(anyString())).thenReturn(false);
				when(locationService.createAndSaveLocation(any(Metadata.class))).thenReturn(buildLocation());
				when(cameraModelService.createAndSaveCameraModel(any(Metadata.class))).thenReturn(buildCameraModel());
				when(orientationService.createAndSaveOrientation(any(Metadata.class))).thenReturn(buildOrientation());

				// --- WHEN ---
				service.createAndSavePhoto(createTestPhotoFile());

				// --- THEN ---
				verify(repository).saveAndFlush(any(Photo.class));
			}
		}

		@Test
		void shouldReturnExistingPhoto() throws MetadataParserException, IOException, GpsResolverException {
			// --- GIVEN ---
			when(repository.existsByHashValue(anyString())).thenReturn(true);
			when(repository.findByHashValue(anyString())).thenReturn(Optional.of(buildPhoto()));

			// --- WHEN ---
			service.createAndSavePhoto(createTestPhotoFile());

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Photo.class));
		}
	}
}