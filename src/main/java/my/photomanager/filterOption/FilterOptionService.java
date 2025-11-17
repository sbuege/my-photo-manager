package my.photomanager.filterOption;

import java.util.Collection;
import my.photomanager.photo.PhotoRepository;
import org.springframework.stereotype.Service;

@Service
public class FilterOptionService {

	private final PhotoRepository photoRepository;

	protected FilterOptionService(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}

	public Collection<CameraSettingsFilter> getCameraSettingsFilters() {
		return photoRepository.countPhotosGroupByCameraSettings()
				.stream()
				.filter(filter -> filter.getModelName() != null)
				.toList();
	}

	public Collection<LocationFilter> getLocationFilters() {
		return photoRepository.countPhotosGroupedByLocation()
				.stream()
				.filter(filter -> filter.getCountry() != null)
				.toList();
	}

	public Collection<CreationDateFilter> getCreationDateFilters() {
		return photoRepository.countPhotosGroupedByCreationYear()
				.stream()
				.filter(filter -> filter.getYear() != null)
				.toList();
	}

	public Collection<OrientationFilter> getOrientationFilters() {
		return photoRepository.countPhotosGroupByOrientation()
				.stream()
				.filter(filter -> filter.getOrientation() != null)
				.toList();
	}
}
