package my.photomanager.library;

import java.time.Instant;

public record LibraryDto(long id, String name, String lastScan, int numberOfPhotos) {

}
