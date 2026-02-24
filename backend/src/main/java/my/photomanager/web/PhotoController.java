package my.photomanager.web;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.filter.FilterService;
import my.photomanager.core.photo.Photo;
import my.photomanager.core.photo.PhotoService;
import my.photomanager.core.photo.PhotoServiceException;
import my.photomanager.core.tag.Tag;
import my.photomanager.core.tag.TagService;
import my.photomanager.web.response.PhotoResponse;
import my.photomanager.web.response.TagResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/photos"})
@Log4j2
public class PhotoController {

    private final PhotoService photoService;
    private final FilterService filterService;
    private final TagService tagService;


    @ExceptionHandler(PhotoServiceException.class)
    public ResponseEntity<String> handlePhotoServiceExceptionException(PhotoServiceException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    /**
     * Retrieves a list of all photos and maps them to {@code PhotoResponse} objects.
     *
     * The photos are filtered based on external tag IDs, but no tag filter is applied in this method.
     *
     * @return a {@code ResponseEntity} containing a list of {@code PhotoResponse} objects,
     *         which represents the details of the retrieved photos.
     */
    @GetMapping("/")
    protected ResponseEntity<List<PhotoResponse>> getPhotos() {
        log.info("Getting all photos");

        return ResponseEntity.ok(
                filterService
                        .filterPhotosByExternalTagIds(Lists.newArrayList()).stream()
                        .map(this::map2PhotoResponse)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Filters photos based on the provided list of external tag IDs and maps the results
     * to {@code PhotoResponse} objects.
     *
     * @param externalTagIds a list of external tag IDs to filter photos by
     * @return a {@code ResponseEntity} containing a list of {@code PhotoResponse} objects,
     *         which represent the details of the filtered photos
     */
    @GetMapping("/byTags")
    protected ResponseEntity<List<PhotoResponse>> filterByTags(List<String> externalTagIds) {
        log.info("Filtering by tags: {}", externalTagIds);

        return ResponseEntity.ok(
                filterService
                        .filterPhotosByExternalTagIds(externalTagIds)
                        .stream()
                        .map(this::map2PhotoResponse)
                        .collect(Collectors.toList())
        );
    }

    /**
     * Retrieves the thumbnail image of a photo identified by its external ID.
     *
     * The method fetches the photo associated with the provided external ID and generates its thumbnail.
     * The response is returned as a JPEG image in a byte array format, with the appropriate content type.
     *
     * @param externalId the unique identifier for the photo whose thumbnail is to be retrieved
     * @return a {@code ResponseEntity} containing the thumbnail as a byte array with the content type
     *         set to {@code MediaType.IMAGE_JPEG}
     * @throws IOException if an I/O error occurs during thumbnail generation or retrieval
     */
    @GetMapping("/thumbnail/{externalId}")
    protected ResponseEntity<byte[]> getPhotoThumbnail(@PathVariable String externalId) throws IOException {
        log.info("Getting thumbnail for photo with externalId: {}", externalId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(getThumbnail(externalId));
    }

    private byte[] getThumbnail(@NonNull String externalId) throws IOException {
        var photo = photoService.findByExternalId(externalId);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Thumbnails.of(Path.of(photo.getFileName())
                        .toFile())
                .scale(1)
                .toOutputStream(out);

        Base64.getEncoder()
                .encodeToString(out.toByteArray());

        return out.toByteArray();
    }

    private PhotoResponse map2PhotoResponse(Photo photo) {
        return new PhotoResponse(photo.getExternalId(), tagService.getPhotoTags(photo).stream().map(this::map2TagResponse).collect(Collectors.toList()));
    }


    private TagResponse map2TagResponse(Tag tag) {
        return new TagResponse(tag.externalId(),
                tag.name());
    }
}
