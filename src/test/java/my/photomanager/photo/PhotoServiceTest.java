package my.photomanager.photo;

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
class PhotoServiceTest {

	// TEST DATA
	final Photo TEST_PHOTO = TestDataBuilder.TestPhotoBuilder.build();

	@Mock
	private PhotoRepository repository;

	@InjectMocks
	private PhotoService service;

	@Test
	@DisplayName("should call saveAndFlush when photo does not exist")
	void shouldCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByHashValue(TestDataBuilder.TEST_PHOTO_HASH_VALUE)).thenReturn(Optional.empty());

		// --- WHEN ---
		service.saveIfPhotoNotExists(TEST_PHOTO);

		// --- THEN ---
		verify(repository).saveAndFlush(TEST_PHOTO);
	}

	@Test
	@DisplayName("should not call saveAndFlush when photo already exists")
	void shouldNotCallSaveAndFlush() {
		// --- GIVEN ---
		when(repository.findByHashValue(TestDataBuilder.TEST_PHOTO_HASH_VALUE)).thenReturn(Optional.of(TEST_PHOTO));

		// --- WHEN ---
		service.saveIfPhotoNotExists(TEST_PHOTO);

		// --- THEN ---
		verify(repository, never()).saveAndFlush(TEST_PHOTO);
	}
}