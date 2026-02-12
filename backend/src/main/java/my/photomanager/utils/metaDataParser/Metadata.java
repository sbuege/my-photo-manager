package my.photomanager.utils.metaDataParser;

import java.time.LocalDate;

public record Metadata(int photoWidth, int photoHeight, String cameraModel, LocalDate creationDate,
					   double gpsLongitude, double gpsLatitude) {

}
