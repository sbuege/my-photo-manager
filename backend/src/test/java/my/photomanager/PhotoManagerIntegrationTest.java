package my.photomanager;

import my.photomanager.core.filter.FilterService;
import my.photomanager.core.library.LibraryService;
import my.photomanager.core.tag.Tag;
import my.photomanager.core.tag.TagPrefix;
import my.photomanager.core.tag.TagService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static my.photomanager.TestDataBuilder.*;
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
        assertThat(library.getNumberOfPhotos()).isEqualTo(5);
        assertThat(library.getLastIndexAt()).isAfter(indexStartTimeStamp);
    }

    @Nested
    class TagServiceTest {
        @Test
        void shouldReturnCameraTags() throws IOException {
            // --- WHEN
            var cameraTags = tagService.getCameraTags();

            // --- THEN
            assertThat(cameraTags).isNotEmpty();
            assertThat(cameraTags).allMatch(tag -> tag.externalId().startsWith(TagPrefix.CAMERA_PREFIX));
        }

        @Test
        void shouldReturnLocationTags() {
            // --- WHEN ---
            var locationTags = tagService.getLocationTags();

            // --- THEN
            assertThat(locationTags).isNotEmpty();
            assertThat(locationTags).allMatch(tag -> tag.externalId().startsWith(TagPrefix.LOCATION_PREFIX));
        }

        @Test
        void shouldReturnOrientationTags() {
            // --- WHEN ---
            var orientationTags = tagService.getOrientationTags();

            // --- THEN
            assertThat(orientationTags).isNotEmpty();
            assertThat(orientationTags).allMatch(tag -> tag.externalId().startsWith(TagPrefix.ORIENTATION_PREFIX));
        }

        @Test
        void shouldReturnCreationYearTags() {
            // --- WHEN ---
            var creationYearTags = tagService.getCreationYearTags();

            // --- THEN
            assertThat(creationYearTags).isNotEmpty();
            assertThat(creationYearTags).allMatch(tag -> tag.externalId().startsWith(TagPrefix.YEAR_PREFIX));
        }
    }


    @Nested
    class FilterServiceTest {

        @Test
        void shouldFilterPhotsByCameraTag() {
            // --- GIVEN ---
            var cameraTags = tagService.getCameraTags();
            assertThat(cameraTags).isNotEmpty();
            var cameraExternalTag = cameraTags.getFirst().externalId();

            // --- WHEN ---
            var photos = filterService.filterPhotosByExternalTagIds(List.of(cameraExternalTag));

            // --- THEN ---
            assertThat(photos).isNotEmpty();
            assertThat(photos).allMatch(photo -> photo.getCameraModel().getExternalId().contains(cameraExternalTag));
        }

        @Test
        void shouldFilterPhotsByLocationTag() {
            // --- GIVEN ---
            var locationTags = tagService.getLocationTags();
            assertThat(locationTags).isNotEmpty();
            var locationExternalTag = locationTags.getFirst().externalId();

            // --- WHEN ---
            var photos = filterService.filterPhotosByExternalTagIds(List.of(locationExternalTag));

            // --- THEN ---
            assertThat(photos).isNotEmpty();
            assertThat(photos).allMatch(photo -> photo.getLocation().getExternalId().contains(locationExternalTag));
        }

        @Test
        void shouldFilterPhotsByCreationYearTag() {
            // --- GIVEN ---
            var creationYearTags = tagService.getCreationYearTags();
            assertThat(creationYearTags).isNotEmpty();
            var creationYearExternalTag = creationYearTags.getFirst().externalId();
            var creationYear = Integer.parseInt(creationYearTags.getFirst().name());

            // --- WHEN ---
            var photos = filterService.filterPhotosByExternalTagIds(List.of(creationYearExternalTag));

            // --- THEN ---
            assertThat(photos).isNotEmpty();
            assertThat(photos).allMatch(photo -> photo.getCreationDate().getYear() == creationYear);
        }

        @Test
        void shouldFilterPhotsByOrientationTag() {
            // --- GIVEN ---
            var orientationTags = tagService.getOrientationTags();
            assertThat(orientationTags).isNotEmpty();
            var orientationExternalTag = orientationTags.getFirst().externalId();
            System.out.println(orientationExternalTag);

            // --- WHEN ---
            var photos = filterService.filterPhotosByExternalTagIds(List.of(orientationExternalTag));

            // --- THEN ---
            assertThat(photos).isNotEmpty();
            assertThat(photos).allMatch(photo -> photo.getOrientation().getExternalId().contains(orientationExternalTag));
        }


        @Test
        void shouldFilterPhotosByCameraModelAndCreationYearTag() {
            var cameraTags = tagService.getCameraTags();
            var cameraExternalTag = cameraTags.stream().filter(tag -> tag.name().equals(TestPhoto004CameraModel)).map(Tag::externalId).findFirst().orElseThrow();

            var creationYearTags = tagService.getCreationYearTags();
            var creationYearExternalTag = creationYearTags.stream().filter(tag -> tag.name().contains(TestPhoto004CreationDate.getYear() + "")).findFirst().orElseThrow();
            var creationYear = Integer.parseInt(creationYearExternalTag.name());

            var photos = filterService.filterPhotosByExternalTagIds(List.of(cameraExternalTag, creationYearExternalTag.externalId()));
            assertThat(photos).isNotEmpty();
            assertThat(photos).allMatch(photo -> photo.getCameraModel().getExternalId().contains(cameraExternalTag)
                    && photo.getCreationDate().getYear() == creationYear);
        }

        @Test
        void shouldFilterPhotosByLocationAndOrientationTag() {
            var locationTags = tagService.getLocationTags();
            var locationExternalTag = locationTags.stream().filter(tag -> tag.name().equals(TestPhoto001LocationCity)).map(Tag::externalId).findFirst().orElseThrow();

            var orientationTags = tagService.getOrientationTags();
            var orientationExternalTag = orientationTags.stream().map(Tag::externalId).findFirst().orElseThrow();
            // TODO add test photos with Portrait orientation

            var photos = filterService.filterPhotosByExternalTagIds(List.of(locationExternalTag, orientationExternalTag));
            assertThat(photos).isNotEmpty();
            assertThat(photos).allMatch(photo -> photo.getLocation().getExternalId().contains(locationExternalTag)
                    && photo.getOrientation().getExternalId().contains(orientationExternalTag));
        }
    }
}
