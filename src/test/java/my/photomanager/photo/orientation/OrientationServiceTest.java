package my.photomanager.photo.orientation;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import my.photomanager.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrientationServiceTest {

	// TEST DATA
	final Orientation TEST_ORIENTATION = TestDataBuilder.TestOrientationBuilder.build();

	@Mock
	private OrientationRepository repository;

	@InjectMocks
	private OrientationService service;

	@Test
	@DisplayName("should call saveAndFlush when orientation does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByName(TEST_ORIENTATION.getName())).thenReturn(Optional.empty());

		// --- WHEN ---
		service.saveOrGetOrientation(TEST_ORIENTATION);

		// --- THEN ---
		verify(repository).saveAndFlush(TEST_ORIENTATION);
	}

	@Test
	@DisplayName("should not call saveAndFlush when orientation already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByName(TEST_ORIENTATION.getName())).thenReturn(Optional.of(TEST_ORIENTATION));

		// --- WHEN ---
		service.saveOrGetOrientation(TEST_ORIENTATION);

		// --- THEN ---
		verify(repository, never()).saveAndFlush(TEST_ORIENTATION);
	}
}