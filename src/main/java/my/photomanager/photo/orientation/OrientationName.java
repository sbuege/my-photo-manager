package my.photomanager.photo.orientation;

import lombok.Getter;

public enum OrientationName {

	LANDSCAPE("Querformat"),
	PORTRAIT("Hochformat"),
	SQUARE("Quadrat");

	@Getter
	private final String name;

	OrientationName(String name) {
		this.name = name;
	}
}
