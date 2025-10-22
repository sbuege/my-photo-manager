package my.photomanager.filter;

import java.util.Collection;
import lombok.NonNull;
import my.photomanager.photo.album.PhotoAlbumDTO;
import my.photomanager.photo.album.PhotoAlbumRepository;
import my.photomanager.photo.cameraSettings.CameraSettingsRepository;
import my.photomanager.photo.category.PhotoCategoryRepository;
import my.photomanager.photo.location.PhotoLocationRepository;
import org.springframework.stereotype.Service;

@Service
public class FilterService {

	private final PhotoAlbumRepository albumRepository;
	private final CameraSettingsRepository cameraSettingsRepository;
	private final PhotoCategoryRepository categoryRepository;
	private final PhotoLocationRepository locationRepository;

	protected FilterService(@NonNull PhotoAlbumRepository albumRepository, @NonNull CameraSettingsRepository cameraSettingsRepository,
			@NonNull PhotoCategoryRepository categoryRepository,
			@NonNull PhotoLocationRepository locationRepository) {
		this.albumRepository = albumRepository;
		this.cameraSettingsRepository = cameraSettingsRepository;
		this.categoryRepository = categoryRepository;
		this.locationRepository = locationRepository;
	}




}
