package my.photomanager.web;

import java.util.List;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.cameraModel.CameraModelStatistic;
import my.photomanager.core.location.LocationStatistic;
import my.photomanager.core.orientation.OrientationStatistic;
import my.photomanager.web.filter.FilterService;
import my.photomanager.web.response.CameraModelResponse;
import my.photomanager.web.response.LocationResponse;
import my.photomanager.web.response.OrientationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping({"/filter"})
@Log4j2
public class FilterController {

	private final FilterService filterService;

	@GetMapping("/cameraFilter")
	protected ResponseEntity<List<CameraModelResponse>> getCameraFilters() {
		Function<CameraModelStatistic, CameraModelResponse> map2CameraModelResponse = (modelStatistic) -> new CameraModelResponse(modelStatistic.getID(),
				modelStatistic.getName());

		return ResponseEntity.ok(filterService.getCameraModelStatistics()
				.stream()
				.map(map2CameraModelResponse)
				.toList());
	}

	@GetMapping("/locationFilter")
	protected ResponseEntity<List<LocationResponse>> getLocationFilters() {
		Function<LocationStatistic, LocationResponse> map2LocationResponse = (locationStatistic -> new LocationResponse(locationStatistic.getID(),
				locationStatistic.getCountry(), locationStatistic.getCity()));

		return ResponseEntity.ok(filterService.getLocationStatistics()
				.stream()
				.map(map2LocationResponse)
				.toList());
	}

	@GetMapping("/orientationFilter")
	protected ResponseEntity<List<OrientationResponse>> getOrientationFilters() {
		Function<OrientationStatistic, OrientationResponse> map2OrientationResponse = (orientationStatistic -> new OrientationResponse(
				orientationStatistic.getID(), orientationStatistic.getName()));

		return ResponseEntity.ok(filterService.getOrientationStatistics()
				.stream()
				.map(map2OrientationResponse)
				.toList());
	}
}
