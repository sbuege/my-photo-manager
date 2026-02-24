package my.photomanager.core.filter;

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
     * Filters photos based on the provided list of external tag IDs. The filtering is applied
     * to the external IDs of both the location and camera model associated with the photos.
     * The resulting list is sorted by the creation date in descending order.
     *
     * @param externalTagIds a list of external tag IDs to filter photos; if the list is
     *                       null or empty, no filtering is applied.
     * @return a list of photos that match the provided external tag IDs, or an empty list
     *         if no matching photos are found.
     */
    public List<Photo> filterPhotosByExternalTagIds(List<String> externalTagIds) {
        Specification<Photo> spec = Specification.where(null);

        spec = spec.and(filterPhotosByLocationExternalTagIds(externalTagIds));
        spec = spec.and(filterPhotosByCameraModelExternalTagIds(externalTagIds));

        var photos = repository.findAll(spec, Sort.by(Sort.Direction.DESC, "creationDate"));

        return photos.stream()
                .toList();
    }

    private Specification<Photo> filterPhotosByLocationExternalTagIds(List<String> externalTagIds) {
        return (root, query, cb) -> {
            if (externalTagIds == null || externalTagIds.isEmpty()) {
                return null;
            }

            return cb.and(root.get("location")
                    .get("externalId")
                    .in(externalTagIds));
        };
    }

    private Specification<Photo> filterPhotosByCameraModelExternalTagIds(List<String> externalTagIds) {
        return (root, query, cb) -> {
            if (externalTagIds == null || externalTagIds.isEmpty()) {
                return null;
            }

            return root
                    .get("cameraModel")
                    .get("externalId")
                    .in(externalTagIds);
        };
    }
}

