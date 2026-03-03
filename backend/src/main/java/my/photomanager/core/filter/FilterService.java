package my.photomanager.core.filter;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.photo.Photo;
import my.photomanager.core.photo.PhotoRepository;
import my.photomanager.core.tag.TagPrefix;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class FilterService {

    private final PhotoRepository repository;

    /**
     * Filters photos based on the provided list of external tag IDs. The filtering is applied
     * to the external IDs of both the location and camera model associated with the photos.
     * The resulting list is sorted by the creation date in descending order.
     *
     * @param externalTagIds a list of external tag IDs to filter photos; if the list is
     *                       null or empty, no filtering is applied.
     * @return a list of photos that match the provided external tag IDs, or an empty list
     * if no matching photos are found.
     */
    public List<Photo> filterPhotosByExternalTagIds(List<String> externalTagIds) {
        Specification<Photo> spec = Specification.where(null);

        List<String> cameraTagIds = externalTagIds.stream()
                .filter(externalTagId -> externalTagId.startsWith(TagPrefix.CAMERA_PREFIX))
                .toList();
        if (!cameraTagIds.isEmpty()) {
            spec = spec.and(filterPhotosByCameraModelExternalTagIds(cameraTagIds));
        }

        List<String> locationTagIds = externalTagIds.stream()
                .filter(externalTagId -> externalTagId.startsWith(TagPrefix.LOCATION_PREFIX))
                .toList();
        if (!locationTagIds.isEmpty()) {
            spec = spec.and(filterPhotosByLocationExternalTagIds(locationTagIds));
        }

        List<String> creationYears = externalTagIds.stream()
                .filter(externalTagId -> externalTagId.startsWith(TagPrefix.YEAR_PREFIX))
                .map(externalTagId -> externalTagId.substring(TagPrefix.YEAR_PREFIX.length()))
                .toList();
        if (!creationYears.isEmpty()) {
            spec = spec.and(filterPhotosByCreationYear(creationYears));
        }

        List<String> orientationTagIds = externalTagIds.stream()
                .filter(externalTagId -> externalTagId.startsWith(TagPrefix.ORIENTATION_PREFIX))
                .toList();
        if (!orientationTagIds.isEmpty()) {
            spec = spec.and(filterByOrientation(orientationTagIds));
        }

        var photos = repository.findAll(spec, Sort.by(Sort.Direction.DESC, "creationDate"));

        return photos.stream()
                .toList();
    }


    private Specification<Photo> filterPhotosByLocationExternalTagIds(List<String> externalLocationTagIds) {
        return (root, query, cb) -> {
            if (externalLocationTagIds == null || externalLocationTagIds.isEmpty()) {
                return null;
            }

            return cb.and(root.get("location")
                    .get("externalId")
                    .in(externalLocationTagIds));
        };
    }

    private Specification<Photo> filterPhotosByCameraModelExternalTagIds(List<String> externalCameraTagIds) {
        return (root, query, cb) -> {
            if (externalCameraTagIds == null || externalCameraTagIds.isEmpty()) {
                return null;
            }

            return root
                    .get("cameraModel")
                    .get("externalId")
                    .in(externalCameraTagIds);
        };
    }

    private Specification<Photo> filterPhotosByCreationYear(List<String> creationYears) {
        return (root, query, criteriaBuilder) -> {

            if (creationYears == null || creationYears.isEmpty()) {
                return null;
            }

            List<Predicate> predicates = creationYears.stream()
                    .map(Integer::valueOf)
                    .map(year -> {
                        LocalDate start = LocalDate.of(year, 1, 1);
                        LocalDate end = LocalDate.of(year, 12, 31);
                        return criteriaBuilder.between(
                                root.get("creationDate"),
                                start,
                                end
                        );
                    })
                    .toList();

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Photo> filterByOrientation(List<String> orientationTags){
        return (root, query, criteriaBuilder) -> {
            if (orientationTags == null || orientationTags.isEmpty()) {
                return null;
            }

            return root.get("orientation")
                    .get("externalId")
                    .in(orientationTags);
        };
    }
}

