package my.photomanager.core.album;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@ToString
@Getter
public class Album {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "external_id", nullable = false, unique = true,  updatable = false, length = 75)
	private String externalId;

	@Column(name = "name", nullable = false, unique = true)
	@NonNull
	@Setter
	private String name;

	@PrePersist
	void prePersist() {
		if (externalId == null || externalId.isBlank()) {
			externalId = UUID.randomUUID().toString();
		}
	}
}
