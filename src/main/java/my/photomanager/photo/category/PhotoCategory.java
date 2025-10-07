package my.photomanager.photo.category;

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

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "photoalbum")
public class PhotoCategory {

	@Id
	@GeneratedValue
	@Column(updatable = false, unique = true)
	private long id;

	@Column(unique = true, nullable = false)
	@NonNull
	private String name;
}
