package my.photomanager.photo.location;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "location", uniqueConstraints = @UniqueConstraint(columnNames = {"country", "city"}))
@ToString
public class Location {

	@Id
	@GeneratedValue
	@Column(updatable = false, unique = true)
	private long id;

	@NonNull
	@Getter
	@Setter
	private String country;

	@NonNull
	@Getter
	@Setter
	private String city;
}
