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
import my.photomanager.photo.cameraSettings.CameraSettings;
import my.photomanager.photo.location.PhotoLocation;

@Builder(setterPrefix = "with")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
@Table(name = "photo")
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
	private String fileName;

	private int height;

	private int width;

	@NonNull
	private LocalDate creationDate;

	@ManyToOne(fetch = FetchType.EAGER)
	private CameraSettings cameraSettings;

	@ManyToOne(fetch = FetchType.EAGER)
	private PhotoLocation location;
}
