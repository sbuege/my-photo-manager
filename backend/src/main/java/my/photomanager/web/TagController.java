package my.photomanager.web;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.tag.Tag;
import my.photomanager.core.tag.TagService;
import my.photomanager.web.response.TagResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/tag"})
@Log4j2
public class TagController {

    private final TagService tagService;

    @GetMapping("/cameraTags")
    protected ResponseEntity<List<TagResponse>> getCameraTags() {
        return ResponseEntity.ok(tagService.getCameraTags()
                .stream()
                .map(this::map2TagResponse)
                .toList());
    }

    @GetMapping("/locationTags")
    protected ResponseEntity<List<TagResponse>> getLocationFilters() {
        return ResponseEntity.ok(tagService.getLocationTags()
                .stream()
                .map(this::map2TagResponse)
                .toList());
    }

    @GetMapping("/creationYearTags")
    protected ResponseEntity<List<TagResponse>> getCreationYearTags() {
        return ResponseEntity.ok(tagService.getCreationYearTags()
                .stream()
                .map(this::map2TagResponse)
                .toList());
    }

    @GetMapping("/orientationTags")
    protected ResponseEntity<List<TagResponse>> getOrientationTags() {
        return ResponseEntity.ok(tagService.getOrientationTags()
                .stream()
                .map(this::map2TagResponse)
                .toList());
    }

    private TagResponse map2TagResponse(Tag tag) {
        return new TagResponse(tag.id(),
                tag.name());
    }
}
