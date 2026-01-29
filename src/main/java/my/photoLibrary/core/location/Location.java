package my.photoLibrary.core.location;

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

	@Column(updatable = false)
	@NonNull
	@Setter
	private String country;

	@Column(updatable = false)
	@NonNull
	@Setter
	private String city;
}
