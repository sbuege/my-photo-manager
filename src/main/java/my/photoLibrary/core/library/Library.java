package my.photoLibrary.core.library;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Getter
public class Library {

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	@NonNull
	@Setter
	private String name;

	@Column(updatable = false, unique = true)
	@NonNull
	private String path;

	@Setter
	private Instant lastIndexAt;

	@Setter
	private int numberOfPhotos;
}
