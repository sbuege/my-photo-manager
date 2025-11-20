package my.photomanager.photo;

import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import my.photomanager.filterOption.CameraModelFilter;
import my.photomanager.filterOption.CreationDateFilter;
import my.photomanager.filterOption.LocationFilter;
import my.photomanager.filterOption.OrientationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

	Optional<Photo> findByHashValue(@NonNull String hashValue);

	boolean existsByHashValue(@NonNull String hashValue);

	@Query("""
			SELECT 
				p.cameraModel.id AS ID,
				p.cameraModel.name AS modelName,
				COUNT(p) AS numberOfPhotos 
				FROM Photo p
				GROUP BY p.cameraModel
			""")
	Collection<CameraModelFilter> groupPhotosByCameraModel();


	@Query("""
			SELECT 
				p.location.id AS ID,
				p.location.country AS country,
				p.location.city AS city,
				COUNT(p) AS numberOfPhotos 
			FROM Photo p
			GROUP BY p.location
			""")
	Collection<LocationFilter> groupPhotosByLocation();

	@Query("""
					SELECT YEAR(p.creationDate) AS year, COUNT(p) AS numberOfPhotos
					FROM Photo p
					GROUP BY YEAR(p.creationDate)
					ORDER BY YEAR(p.creationDate) DESC
			""")
	Collection<CreationDateFilter> groupPhotosByCreationYear();

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
	Collection<OrientationFilter> groupPhotosByOrientation();
}
