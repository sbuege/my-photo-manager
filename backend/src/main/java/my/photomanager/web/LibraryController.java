package my.photomanager.web;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.library.Library;
import my.photomanager.core.library.LibraryService;
import my.photomanager.core.library.LibraryServiceException;
import my.photomanager.web.response.LibraryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/library"})
@Log4j2
public class LibraryController {

    private final LibraryService libraryService;

    @ExceptionHandler(LibraryServiceException.class)
    public ResponseEntity<String> handleLibraryServiceException(LibraryServiceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @GetMapping("/")
    protected ResponseEntity<List<LibraryResponse>> getLibraries() {
        List<LibraryResponse> libraryResponseList = libraryService.getAllLibraries()
                .stream()
                .map(this::map2LibraryResponse)
                .toList();

        return ResponseEntity.ok(libraryResponseList);
    }

    @PostMapping("/add")
    protected ResponseEntity<Void> addLibrary(@RequestParam @NotBlank String name, @RequestParam @NotBlank String path) {
        libraryService.createAndSaveLibrary(name, path);

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/index/{ID}")
    protected ResponseEntity<Void> indexLibrary(@PathVariable long ID) throws IOException {
        libraryService.indexLibrary(ID);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/edit/{ID}")
    protected ResponseEntity<Void> editLibrary(@PathVariable long ID, @RequestParam @NotBlank String name) {
        libraryService.editLibrary(ID, name);

        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/delete/{ID}")
    protected ResponseEntity<Void> deleteLibrary(@PathVariable long ID) {
        libraryService.deleteLibrary(ID);

        return ResponseEntity.ok()
                .build();
    }

    private LibraryResponse map2LibraryResponse(Library library) {
        return new LibraryResponse(library.getExternalId(), library.getName(), library.getLastIndexAt()
                .toString(), library.getNumberOfPhotos());
    }


}
