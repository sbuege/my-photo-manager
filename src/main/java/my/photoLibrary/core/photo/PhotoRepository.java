package my.photoLibrary.core.photo;

import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import my.photoLibrary.core.cameraModel.CameraModelStatistic;
import my.photoLibrary.core.location.LocationStatistic;
import my.photoLibrary.core.orientation.OrientationStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

	boolean existsByHashValue(@NonNull String hashValue);

	Optional<Photo> findByHashValue(String hashValue);

	@Query("""
			SELECT
				p.cameraModel.id AS ID,
				p.cameraModel.name AS name,
				COUNT(p) AS numberOfPhotos
				FROM Photo p
				GROUP BY p.cameraModel
			""")
	Collection<CameraModelStatistic> groupPhotosByCameraModel();


	@Query("""
			SELECT
				p.location.id AS ID,
				p.location.country AS country,
				p.location.city AS city,
				COUNT(p) AS numberOfPhotos 
			FROM Photo p
			GROUP BY p.location
			""")
	Collection<LocationStatistic> groupPhotosByLocation();

	@Query("""
					SELECT YEAR(p.creationDate) AS year, COUNT(p) AS numberOfPhotos
					FROM Photo p
					GROUP BY YEAR(p.creationDate)
					ORDER BY YEAR(p.creationDate) DESC
			""")
	Collection<CreationYearStatistic> groupPhotosByCreationYear();

	@Query("""
				SELECT
					p.orientation.id AS ID,
					p.orientation.name AS name,
				    COUNT(p) AS numberOfPhotos
				FROM Photo p
				GROUP BY p.orientation
			\t""")
	Collection<OrientationStatistic> groupPhotosByOrientation();

}
