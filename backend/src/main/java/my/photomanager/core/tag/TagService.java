package my.photomanager.core.tag;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import my.photomanager.core.photo.Photo;
import my.photomanager.core.photo.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
@Log4j2
public class TagService {

    private final PhotoRepository repository;

    /**
     * Retrieves a list of tags representing camera models associated with photos.
     * Each tag corresponds to the ID and name of a camera model, with the data
     * aggregated by the camera model.
     *
     * @return a list of Tag objects, where each object contains the ID and name
     * of a camera model associated with the photos.
     */
    public List<Tag> getCameraTags() {
        return repository.getCameraTags()
                .stream()
                .toList();
    }

    /**
     * Retrieves a list of location tags from the PhotoRepository, combining tags
     * for both countries and cities. The resulting list contains distinct Tag
     * objects that aggregate the location IDs and names.
     *
     * @return a list of Tag objects, each representing a unique location (country or city).
     */
    public List<Tag> getLocationTags() {
        return Stream.concat(
                repository.getLocationCountryTags()
                        .stream(),
                repository.getLocationCityTags()
                        .stream()
        ).distinct().toList();
    }

    /**
     * Retrieves a list of tags representing the creation years of photos.
     * Each tag contains the ID associated with the camera model, the tag type
     * as {@code CREATION_YEAR_TAG}, and the year derived from the creation date
     * of the photo.
     *
     * @return a list of Tag objects, each representing a distinct creation year
     * of photos.
     */
    public List<Tag> getCreationYearTags() {
        return repository.getCreationYearTags()
                .stream()
                .toList();
    }

    /**
     * Retrieves a list of tags representing the orientation of photos.
     * Each tag corresponds to the ID and name of a photo orientation,
     * with the tag type specified as {@code ORIENTATION_TAG}.
     *
     * @return a list of Tag objects, where each object represents
     * a unique photo orientation.
     */
    public List<Tag> getOrientationTags() {
        return repository.getOrientationTags()
                .stream()
                .toList();
    }

    /**
     * Retrieves a list of tags associated with a given photo. Tags are generated
     * from various properties of the photo, such as camera model, location,
     * creation date, and orientation.
     *
     * @param photo the photo for which tags are to be generated
     * @return a list of Tag objects representing different attributes of the photo,
     * including camera model, location (country and city), creation year,
     * and orientation
     */
    public List<Tag> getPhotoTags(Photo photo) {

        List<Tag> tags = Lists.newArrayList();

        var cameraModel = photo.getCameraModel();

        var cameraModelTag = new Tag(cameraModel.getId(), TagType.ORIENTATION_TAG, cameraModel.getName());
        tags.add(cameraModelTag);


        var location = photo.getLocation();
        var locationCountryTag = new Tag(location.getId(), TagType.LOCATION_TAG, location.getCountry());
        var locationCityTag = new Tag(location.getId(), TagType.LOCATION_TAG, location.getCity());
        tags.add(locationCountryTag);
        tags.add(locationCityTag);

        var creationDate = photo.getCreationDate();
        if (creationDate != null) {
            var createYearTag = new Tag(0, TagType.CREATION_YEAR_TAG, creationDate.getYear() + "");
            tags.add(createYearTag);
        }

        var orientation = photo.getOrientation();
        var orientationTag = new Tag(orientation.getId(), TagType.ORIENTATION_TAG, orientation.getName());
        tags.add(orientationTag);

        return tags;
    }


}
