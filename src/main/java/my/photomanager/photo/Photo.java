package my.photomanager.photo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import my.photomanager.photo.cameraModel.CameraModel;
import my.photomanager.photo.location.Location;

@Builder(setterPrefix = "with", toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
@Table(name = "photo")
@ToString
public class Photo {

	@Id
	@GeneratedValue
	@Column(updatable = false, unique = true)
	@Getter
	private long id;

	@NonNull
	@Column(updatable = false, unique = true)
	private String hashValue;

	@NonNull
	@Column(updatable = false)
	private String fileName;

	@NonNull
	@Column(updatable = false)
	private Integer height;

	@NonNull
	@Column(updatable = false)
	private Integer width;

	@Column(name = "creationDate", columnDefinition = "DATE")
	private LocalDate creationDate;

	@ManyToOne(fetch = FetchType.EAGER)
	private CameraModel cameraModel;

	@ManyToOne(fetch = FetchType.EAGER)
	private Location location;
}
