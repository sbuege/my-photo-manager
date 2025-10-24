package my.photomanager.filter;

import java.util.Collection;
import my.photomanager.photo.PhotoRepository;
import org.springframework.stereotype.Service;

@Service
public class FilterService {

	private final PhotoRepository photoRepository;

	protected FilterService(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}

	public Collection<CameraSettingsFilter> getCameraSettingsFilters() {
		return photoRepository.countPhotosGroupByCameraSettings();
	}

	public Collection<LocationFilter> getLocationFilters() {
		return photoRepository.countPhotosGroupedByLocation();
	}

	public Collection<CreationDateFilter> getCreationDateFilters() {
		return photoRepository.countPhotosGroupedByCreationYear();
	}

	public Collection<OrientationFilter> getOrientationFilters() {
		return photoRepository.countPhotosGroupByOrientation();
	}
}
