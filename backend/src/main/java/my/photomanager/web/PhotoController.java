package my.photomanager.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.cameraModel.CameraModelStatistic;
import my.photomanager.core.photo.Photo;
import my.photomanager.core.photo.PhotoService;
import my.photomanager.core.photo.PhotoServiceException;
import my.photomanager.web.filter.ActiveFilter;
import my.photomanager.web.filter.FilterService;
import my.photomanager.web.response.CameraModelResponse;
import my.photomanager.web.response.PhotoResponse;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/photos"})
@Log4j2
public class PhotoController {

    private final PhotoService photoService;
    private final FilterService filterService;


    @ExceptionHandler(PhotoServiceException.class)
    public ResponseEntity<String> handlePhotoServiceExceptionException(PhotoServiceException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @GetMapping("/")
    protected ResponseEntity<List<PhotoResponse>> getPhotos() {
        Function<Photo, PhotoResponse> map2PhotoResponse = (photo) -> new PhotoResponse(photo.getId(), photo.getCameraModel().getName());

        var emptyActiveFilter = new ActiveFilter(List.of(), List.of(), List.of(), List.of());


        return ResponseEntity.ok(filterService.filterPhotos(emptyActiveFilter).stream().map(map2PhotoResponse).collect(Collectors.toList()));
    }

    @GetMapping("/thumbnail/{ID}")
    protected ResponseEntity<byte[]> getPhotoThumbnail(@PathVariable long ID) throws IOException {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(getThumbnail(ID));
    }

    private byte[] getThumbnail(@NonNull Long ID) throws IOException {
        var photo = photoService.findById(ID);
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
