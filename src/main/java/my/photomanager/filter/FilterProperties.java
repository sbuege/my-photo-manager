package my.photomanager.filter;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record FilterProperties(List<String> locationCountries, List<String> locationCities, LocalDate startDate, LocalDate endDate, List<String> cameraModels) {

}
