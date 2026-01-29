package my.photomanager.core.orientation;

import static my.photomanager.TestDataBuilder.TestPhotoHeight;
import static my.photomanager.TestDataBuilder.TestPhotoWidth;
import static my.photomanager.TestDataBuilder.buildOrientation;
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
class OrientationServiceTest {

	@Mock
	private OrientationRepository repository;

	@InjectMocks
	private OrientationService service;

	@Test
	void shouldReturnAllOrientations() {
		// --- WHEN
		service.getAllOrientations();

		// --- THEN
		verify(repository).findAll();
	}

	@Nested
	class CreateOrientationTest {

		public static Stream<Metadata> metaDataProvider() {
			return Stream.of(
					new Metadata(TestPhotoWidth, TestPhotoHeight, Strings.EMPTY, null, 0.0d, 0.0d),
					new Metadata(TestPhotoWidth, TestPhotoHeight, Strings.EMPTY, null, 0.0d, 0.0d),
					new Metadata(TestPhotoWidth, TestPhotoHeight, Strings.EMPTY, null, 0.0d, 0.0d)
			);
		}

		@ParameterizedTest
		@MethodSource("metaDataProvider")
		void shouldCreateAndSaveOrientationFromMetaData(Metadata metadata) {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(false);

			// --- WHEN ---
			service.createAndSaveOrientation(metadata);

			// --- THEN ---
			verify(repository).saveAndFlush(any(Orientation.class));
		}

		@Test
		void shouldReturnExistingOrientation() {
			// --- GIVEN ---
			when(repository.existsByName(anyString())).thenReturn(true);
			when(repository.findByName(anyString())).thenReturn(Optional.of(buildOrientation()));

			// --- WHEN ---
			var metaData = new Metadata(1024, 768, Strings.EMPTY, null, 0.0d, 0.0d);
			service.createAndSaveOrientation(metaData);

			// --- THEN ---
			verify(repository, never()).saveAndFlush(any(Orientation.class));
		}

		public static Stream<Metadata> invalidMetaDataProvider() {
			return Stream.of(
					new Metadata(0, TestPhotoHeight, Strings.EMPTY, null, 0.0d, 0.0d),
					new Metadata(-1, TestPhotoHeight, Strings.EMPTY, null, 0.0d, 0.0d),
					new Metadata(TestPhotoWidth, 0, Strings.EMPTY, null, 0.0d, 0.0d),
					new Metadata(TestPhotoWidth, -1
							, Strings.EMPTY, null, 0.0d, 0.0d)
			);
		}


		@ParameterizedTest
		@MethodSource("invalidMetaDataProvider")
		void shouldThrowExceptionWhenMetaDataAreInvalid(Metadata metadata) {
			// --- WHEN / THEN ---
			assertThrows(OrientationServiceException.class, () -> service.createAndSaveOrientation(metadata));
			verify(repository, never()).saveAndFlush(any(Orientation.class));
		}
	}
}