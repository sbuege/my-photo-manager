package my.photomanager.web.filter;

import static org.mockito.Mockito.verify;

import my.photomanager.core.photo.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {

	@Mock
	private PhotoRepository repository;

	@InjectMocks
	private FilterService service;


}