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

	public Collection<CameraModelFilter> getCameraModelFilters() {
		return photoRepository.groupPhotosByCameraModel()
				.stream()
				.filter(filter -> filter.getName() != null)
				.toList();
	}

	public Collection<LocationFilter> getLocationFilters() {
		return photoRepository.groupPhotosByLocation()
				.stream()
				.filter(filter -> filter.getCountry() != null)
				.toList();
	}

	public Collection<CreationDateFilter> getCreationYearFilters() {
		return photoRepository.groupPhotosByCreationYear()
				.stream()
				.filter(filter -> filter.getYear() != null)
				.toList();
	}

	public Collection<OrientationFilter> getOrientationFilters() {
		return photoRepository.groupPhotosByOrientation()
				.stream()
				.filter(filter -> filter.getName() != null)
				.toList();
	}
}
