package my.photomanager.core.tag;

import my.photomanager.core.photo.PhotoRepository;
import my.photomanager.web.filter.FilterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static my.photomanager.TestDataBuilder.buildPhoto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private PhotoRepository repository;

    @InjectMocks
    private TagService service;

    @Test
    void shouldReturnCameraTags() {
        // --- WHEN
        service.getCameraTags();

        // --- THEN
        verify(repository).getCameraTags();
    }

    @Test
    void shouldReturnLocationTags() {
        // --- WHEN
        service.getLocationTags();

        // --- THEN
        verify(repository).getLocationCountryTags();
        verify(repository).getLocationCityTags();
    }

    @Test
    void shouldReturnCreationYearTags() {
        // --- WHEN
        service.getCreationYearTags();

        // --- THEN
        verify(repository).getCreationYearTags();
    }

    @Test
    void shouldReturnOrientationTags() {
        // --- WHEN
        service.getOrientationTags();

        // --- THEN
        verify(repository).getOrientationTags();
    }
}