package my.photomanager.web;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.photo.Photo;
import my.photomanager.core.photo.PhotoService;
import my.photomanager.core.photo.PhotoServiceException;
import my.photomanager.core.tag.Tag;
import my.photomanager.core.tag.TagService;
import my.photomanager.web.filter.ActiveFilter;
import my.photomanager.web.filter.FilterService;
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
import java.util.function.Function;
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

    @GetMapping("/")
    protected ResponseEntity<List<PhotoResponse>> getPhotos() {
        Function<Tag, TagResponse> map2TagResponse = (tag) -> new TagResponse(tag.externalId(), tag.name());
        Function<Photo, PhotoResponse> map2PhotoResponse = (photo) -> new PhotoResponse(photo.getExternalId(), tagService.getPhotoTags(photo).stream().map(map2TagResponse).collect(Collectors.toList()));

        var emptyActiveFilter = new ActiveFilter(List.of(), List.of(), List.of(), List.of());
        return ResponseEntity.ok(filterService.filterPhotos(emptyActiveFilter).stream().map(map2PhotoResponse).collect(Collectors.toList()));
    }

    @GetMapping("/thumbnail/{externalId}")
    protected ResponseEntity<byte[]> getPhotoThumbnail(@PathVariable String externalId) throws IOException {
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
}
