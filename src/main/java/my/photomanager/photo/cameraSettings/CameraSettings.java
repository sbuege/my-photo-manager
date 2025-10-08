package my.photomanager.photo.cameraSettings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
@Table(name = "camerasettings")
public class CameraSettings {

	@Id
	@GeneratedValue
	@Column(updatable = false, unique = true)
	private long id;

	@Column(unique = true, nullable = false)
	@NonNull
	private String cameraModelName;
}
