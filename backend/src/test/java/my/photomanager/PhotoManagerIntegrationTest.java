package my.photomanager;

import my.photomanager.core.library.LibraryService;
import my.photomanager.core.tag.TagService;
import my.photomanager.web.filter.FilterService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Instant;

import static my.photomanager.TestDataBuilder.TestFilePath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@SpringBootTest
@TestInstance(PER_CLASS)
public class PhotoManagerIntegrationTest {

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private FilterService filterService;

    @Autowired
    private TagService tagService;

    @BeforeAll
    void setUp() throws IOException {
        // --- GIVEN ---
        var library = libraryService.createAndSaveLibrary("TestLibrary", TestFilePath.toString());

        // --- WHEN
        var indexStartTimeStamp = Instant.now();
        library = libraryService.indexLibrary(library.getId());

        // --- THEN
        assertThat(library.getNumberOfPhotos()).isEqualTo(4);
        assertThat(library.getLastIndexAt()).isAfter(indexStartTimeStamp);
    }

    @Test
    void shouldReturnCameraTags() throws IOException {
        // --- WHEN
        var cameraTags = tagService.getCameraTags();

        // --- THEN
        assertThat(cameraTags).isNotEmpty();
        //TODO check content
    }

    @Test
    void shouldReturnLocationTags() {
        // --- WHEN ---
        var locationTags = tagService.getLocationTags();

        // --- THEN
        assertThat(locationTags).isNotEmpty();
        //TODO check content
    }

    @Test
    void shouldReturnOrientationTags() {
        // --- WHEN ---
        var orientationTags = tagService.getOrientationTags();

        // --- THEN
        assertThat(orientationTags).isNotEmpty();
        //TODO check content
    }

    @Test
    void shouldReturnCreationYearTags() {
        // --- WHEN ---
        var creationYearTags = tagService.getCreationYearTags();

        // --- THEN
        assertThat(creationYearTags).isNotEmpty();
        //TODO check content
    }
}
