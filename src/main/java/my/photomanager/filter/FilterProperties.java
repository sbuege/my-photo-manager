package my.photomanager.filter;

import java.time.LocalDate;
import java.util.List;


public record FilterProperties(List<Long> locationIds, LocalDate startDate, LocalDate endDate) {

}
