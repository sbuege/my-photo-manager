package my.photomanager.metadata;

import java.time.LocalDate;
import lombok.NonNull;

public record PhotoMetadata(int photoWidth, int photoHeight, @NonNull String cameraModel, @NonNull LocalDate creationDate, double gpsLongitude, double gpsLatitude) {

}
