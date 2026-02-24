package my.photomanager.core.location;

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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"country", "city"}))
@ToString
@Getter
public class Location {

	@Id
	@GeneratedValue
	private long id;

	@Column(name = "external_id", nullable = false, unique = true, updatable = false, length = 36)
	private String externalId;

	@Column(name = "country", nullable = false)
	@NonNull
	@Setter
	private String country;

	@Column(name = "city", nullable = false)
	@NonNull
	@Setter
	private String city;

	@PrePersist
	void prePersist() {
		if (externalId == null || externalId.isBlank()) {
			externalId = UUID.randomUUID().toString();
		}
	}
}
