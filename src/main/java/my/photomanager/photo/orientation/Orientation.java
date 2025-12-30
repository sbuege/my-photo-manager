package my.photomanager.photo.orientation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "orientation")
@ToString
public class Orientation {

	@Id
	@GeneratedValue
	@Column(updatable = false, unique = true)
	@Getter
	private long id;

	@Column(updatable = false, unique = true)
	@NonNull
	@Getter
	private String name;
}
