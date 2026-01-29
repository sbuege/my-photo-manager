package my.photoLibrary.web.filter;

import java.util.List;

public record ActiveFilter(List<Long> locationIds, List<Integer> creationYears, List<Long> cameraModelIds, List<Long> orientationIds) {

}
