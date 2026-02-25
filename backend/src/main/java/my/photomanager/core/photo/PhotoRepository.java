package my.photomanager.core.photo;

import lombok.NonNull;
import my.photomanager.core.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
                             p.cameraModel.name )
                 FROM Photo p
            """)
    Collection<Tag> getCameraTags();

    @Query("""
                SELECT DISTINCT new my.photomanager.core.tag.Tag(
                    p.location.id,
                    p.location.externalId,
                    p.location.country
                )
                FROM Photo p
            """)
    Collection<Tag> getLocationCountryTags();

    @Query("""
                SELECT DISTINCT new my.photomanager.core.tag.Tag(
                    p.location.id,
                    p.location.externalId,
                    p.location.city
                )
                FROM Photo p
            """)
    Collection<Tag> getLocationCityTags();

    @Query("""
                 SELECT DISTINCT new my.photomanager.core.tag.Tag(
                             -1,
                            CONCAT(:yearPrefix, YEAR(p.creationDate)),
                            CONCAT('', YEAR(p.creationDate))
                 )
                 FROM Photo p
                 WHERE p.creationDate IS NOT NULL
            """)
    Collection<Tag> getCreationYearTags(@Param("yearPrefix") String yearPrefix);

    @Query("""
                 SELECT DISTINCT new my.photomanager.core.tag.Tag(
                             p.orientation.id,
                             p.orientation.externalId,
                             p.orientation.name
                 )
                 FROM Photo p
            """)
    Collection<Tag> getOrientationTags();

}
