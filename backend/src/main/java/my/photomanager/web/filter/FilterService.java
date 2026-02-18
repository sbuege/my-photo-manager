package my.photomanager.web.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.photo.Photo;
import my.photomanager.core.photo.PhotoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class FilterService {

    private final PhotoRepository repository;

    /**
     * Filters photos based on the specified criteria provided in the {@code ActiveFilter} object.
     * The filtering can include location IDs, camera model IDs, and orientation IDs.
     * The resulting photos are sorted in descending order of creation date and returned as a list of their IDs.
     *
     * @param activeFilter the filter criteria containing lists of location IDs, camera model IDs,
     *                     and orientation IDs to filter the photos.
     * @return a list of photo IDs that match the specified filtering criteria.
     */
    public List<Photo> filterPhotos(ActiveFilter activeFilter) {
        Specification<Photo> spec = Specification.where(null);

        spec = spec.and(filterPhotosByLocationIds(activeFilter.locationIds()));
        spec = spec.and(filterPhotosByCreationYear(activeFilter.creationYears()));
        spec = spec.and(filterPhotosByCameraModelIds(activeFilter.cameraModelIds()));
        spec = spec.and(filterPhotosByOrientationIds(activeFilter.orientationIds()));

        var photos = repository.findAll(spec, Sort.by(Sort.Direction.DESC, "creationDate"));

        return photos.stream()
                .toList();
    }

    private Specification<Photo> filterPhotosByLocationIds(List<Long> locationIds) {
        return (root, query, cb) -> {
            if (locationIds == null || locationIds.isEmpty()) {
                return null;
            }

            return cb.and(root.get("location")
                    .get("id")
                    .in(locationIds));
        };
    }

    private Specification<Photo> filterPhotosByCreationYear(List<Integer> years) {
        return (root, query, cb) -> {
            if (years == null || years.isEmpty()) {
                return null;
            }

            return root.get("creationDate")
                    .in(years);
        };
    }

    private Specification<Photo> filterPhotosByCameraModelIds(List<Long> cameraModelIds) {
        return (root, query, cb) -> {
            if (cameraModelIds == null || cameraModelIds.isEmpty()) {
                return null;
            }

            return root
                    .get("cameraModel")
                    .get("id")
                    .in(cameraModelIds);
        };
    }

    private Specification<Photo> filterPhotosByOrientationIds(List<Long> orientationIds) {
        return (root, query, cb) -> {
            if (orientationIds == null || orientationIds.isEmpty()) {
                return null;
            }

            return root
                    .get("orientation")
                    .get("id")
                    .in(orientationIds);
        };
    }
}
