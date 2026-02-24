package my.photomanager.core.photo;

import jakarta.persistence.*;
import lombok.*;
import my.photomanager.core.cameraModel.CameraModel;
import my.photomanager.core.location.Location;
import my.photomanager.core.orientation.Orientation;

import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true, setterPrefix = "with", access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Getter
public class Photo {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "external_id", nullable = false, unique = true, updatable = false, length = 36)
    private String externalId;

    @Column(name= "hash_value", nullable = false, unique = true, updatable = false)
    @NonNull
    private String hashValue;

    @Column(name= "file_name",nullable = false, unique = true, updatable = false)
    @NonNull
    private String fileName;

    @Column( name= "height", nullable = false, updatable = false)
    @NonNull
    private Integer height;

    @Column(name= "width", nullable = false, updatable = false)
    @NonNull
    private Integer width;

    @ManyToOne(fetch = FetchType.EAGER)
    private Orientation orientation;

    @ManyToOne(fetch = FetchType.EAGER)
    private CameraModel cameraModel;

    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    @Column(name = "creation_date", columnDefinition = "DATE")
    private LocalDate creationDate;

    @PrePersist
    void prePersist() {
        if (externalId == null || externalId.isBlank()) {
            externalId = UUID.randomUUID().toString();
        }
    }

    public Photo(@NonNull String hashValue, @NonNull String fileName, int photoHeight, int photoWidth) {
        this.hashValue = hashValue;
        this.fileName = fileName;
        this.height = photoHeight;
        this.width = photoWidth;
    }
}
