package my.photomanager.filter;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record FilterProperties(List<Long> locationIDs, LocalDate startDate, LocalDate endDate, List<Long> cameraModelIds) {

}
