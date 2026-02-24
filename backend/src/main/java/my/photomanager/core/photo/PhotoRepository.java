package my.photomanager.core.photo;

import lombok.NonNull;
import my.photomanager.core.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Photo> {

    boolean existsByHashValue(@NonNull String hashValue);

    Optional<Photo> findByExternalId(@NonNull String externalId);

    Optional<Photo> findByHashValue(String hashValue);

    @Query("""
                 SELECT DISTINCT new my.photomanager.core.tag.Tag(
                             p.cameraModel.id,
                             p.cameraModel.externalId,
                             my.photomanager.core.tag.TagType.CAMERA_TAG,
                             p.cameraModel.name )
                 FROM Photo p
            """)
    Collection<Tag> getCameraTags();

    @Query("""
                SELECT DISTINCT new my.photomanager.core.tag.Tag(
                    p.location.id,
                    p.location.externalId,
                    my.photomanager.core.tag.TagType.LOCATION_TAG,
                    p.location.country
                )
                FROM Photo p
            """)
    Collection<Tag> getLocationCountryTags();

    @Query("""
                SELECT DISTINCT new my.photomanager.core.tag.Tag(
                    p.location.id,
                    p.location.externalId,
                    my.photomanager.core.tag.TagType.LOCATION_TAG,
                    p.location.city
                )
                FROM Photo p
            """)
    Collection<Tag> getLocationCityTags();

    @Query("""
                 SELECT DISTINCT new my.photomanager.core.tag.Tag(
                             -1,
                             '',
                             my.photomanager.core.tag.TagType.CREATION_YEAR_TAG,
                             CONCAT('', YEAR(p.creationDate))
                 )
                 FROM Photo p
                 WHERE p.creationDate IS NOT NULL
            """)
    Collection<Tag> getCreationYearTags();

    @Query("""
                 SELECT DISTINCT new my.photomanager.core.tag.Tag(
                             p.orientation.id,
                             p.orientation.externalId,
                             my.photomanager.core.tag.TagType.ORIENTATION_TAG,
                             p.orientation.name
                 )
                 FROM Photo p
            """)
    Collection<Tag> getOrientationTags();

}
