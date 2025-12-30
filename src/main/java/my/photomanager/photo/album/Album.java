package my.photomanager.photo.album;

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
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "album")
@ToString
public class Album {

	@Id
	@GeneratedValue
	@Column(updatable = false, unique = true)
	@Getter
	private long id;

	@Column(updatable = false, unique = true)
	@NonNull
	@Getter
	@Setter
	private String name;
}
