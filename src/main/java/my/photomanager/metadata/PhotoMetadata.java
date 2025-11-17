package my.photomanager.metadata;

import java.time.LocalDate;
import java.util.Optional;
import lombok.Builder;

@Builder(toBuilder = true)
public record PhotoMetadata(Optional<Integer> photoWidth, Optional<Integer> photoHeight, Optional<String> cameraModel, Optional<LocalDate> creationDate,
							Optional<Double> gpsLongitude, Optional<Double> gpsLatitude) {

}
