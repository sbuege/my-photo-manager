package my.photomanager.core.photo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import my.photomanager.core.cameraModel.CameraModel;
import my.photomanager.core.location.Location;
import my.photomanager.core.orientation.Orientation;

@NoArgsConstructor(access = AccessLevel.PROTECTED)

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
	@NonNull
	private Orientation orientation;

	@ManyToOne(fetch = FetchType.EAGER)
	private CameraModel cameraModel;

	@ManyToOne(fetch = FetchType.EAGER)
	private Location location;

	@Column(name = "creationDate", columnDefinition = "DATE")
	private LocalDate creationDate;

	public Photo(@NonNull String hashValue, @NonNull String fileName, @NonNull Integer height, @NonNull Integer width, @NonNull Orientation orientation,
			@NonNull CameraModel cameraModel, @NonNull Location location, LocalDate creationDate) {
		this.hashValue = hashValue;
		this.fileName = fileName;
		this.height = height;
		this.width = width;
		this.orientation = orientation;
		this.cameraModel = cameraModel;
		this.location = location;
		this.creationDate = creationDate;
	}
}
