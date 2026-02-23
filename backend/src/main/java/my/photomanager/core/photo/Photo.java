package my.photomanager.core.photo;

import jakarta.persistence.*;
import lombok.*;
import my.photomanager.core.cameraModel.CameraModel;
import my.photomanager.core.location.Location;
import my.photomanager.core.orientation.Orientation;

import java.time.LocalDate;

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

    @Column(updatable = false, unique = true)
    @NonNull
    private String hashValue;

    @Column(updatable = false)
    @NonNull
    private String fileName;

    @Column(updatable = false)
    @NonNull
    private Integer height;

    @Column(updatable = false)
    @NonNull
    private Integer width;

    @ManyToOne(fetch = FetchType.EAGER)
    private Orientation orientation;

    @ManyToOne(fetch = FetchType.EAGER)
    private CameraModel cameraModel;

    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    @Column(name = "creationDate", columnDefinition = "DATE")
    private LocalDate creationDate;

    public Photo(@NonNull String hashValue, @NonNull String fileName, int photoHeight, int photoWidth) {
        this.hashValue = hashValue;
        this.fileName = fileName;
        this.height = photoHeight;
        this.width = photoWidth;
    }
}
