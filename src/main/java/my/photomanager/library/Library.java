package my.photomanager.library;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "library")
@ToString
public class Library {

	@Id
	@GeneratedValue
	@Column(updatable = false, unique = true)
	@Getter
	private long id;

	@Column(unique = true, nullable = false)
	@NonNull
	@Getter
	@Setter
	private String name;

	@Column(unique = true, nullable = false)
	@NonNull
	@Getter
	@Setter
	private String path;

	@Getter
	@Setter
	private Instant lastScan;

	@Getter
	@Setter
	private int numberOfPhotos;
}
