package my.photomanager.photo;

import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import my.photomanager.filter.CameraSettingsFilter;
import my.photomanager.filter.CreationDateFilter;
import my.photomanager.filter.LocationFilter;
import my.photomanager.filter.OrientationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

	Optional<Photo> findByHashValue(@NonNull String hashValue);

	boolean existsByHashValue(@NonNull String hashValue);

	@Query("""
			SELECT 
				p.cameraSettings.id AS ID,
				p.cameraSettings.cameraModelName AS modelName,
				COUNT(p) AS numberOfPhotos 
				FROM Photo p
				GROUP BY p.cameraSettings
			""")
	Collection<CameraSettingsFilter> countPhotosGroupByCameraSettings();


	@Query("""
			SELECT 
				p.location.id AS ID,
				p.location.country AS country,
				p.location.city AS city,
				COUNT(p) AS numberOfPhotos 
			FROM Photo p
			GROUP BY p.location
			""")
	Collection<LocationFilter> countPhotosGroupedByLocation();

	@Query("""
					SELECT YEAR(p.creationDate) AS year, COUNT(p) AS numberOfPhotos
					FROM Photo p
					GROUP BY YEAR(p.creationDate)
					ORDER BY YEAR(p.creationDate) DESC
			""")
	Collection<CreationDateFilter> countPhotosGroupedByCreationYear();

	@Query("""
			SELECT         
				CASE
			                   WHEN p.height > p.width THEN 'PORTRAIT'
			                   WHEN p.width > p.height THEN 'LANDSCAPE'
			                   ELSE 'SQUARE'
			             END AS orientation,
			    COUNT(p) AS numberOfPhotos
			FROM Photo p
			GROUP BY orientation
			""")
	Collection<OrientationFilter> countPhotosGroupByOrientation();
}
