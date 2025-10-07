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

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "photolocation", uniqueConstraints = @UniqueConstraint(columnNames = {"country", "city"}))
public class PhotoLocation {

	@Id
	@GeneratedValue
	@Column(updatable = false, unique = true)
	private long id;

	@NonNull
	private String country;

	@NonNull
	private String city;
}
