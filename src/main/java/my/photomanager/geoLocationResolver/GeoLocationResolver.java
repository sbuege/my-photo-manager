package my.photomanager.geoLocationResolver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;

@Log4j2
public class GeoLocationResolver {

	private final static String DEFAULT_COUNTRY = Strings.EMPTY;
	private final static String DEFAULT_CITY = Strings.EMPTY;

	/**
	 * Resolves the given geographic coordinates (longitude and latitude)
	 * to their corresponding country and city using the OpenStreetMap Nominatim
	 * reverse geocoding service.
	 *
	 * @param longitude the longitude value
	 * @param latitude  the latitude value
	 * @return a {@link LocationInfo} object containing the resolved country and city
	 */
	public static LocationInfo resolveLongitudeLatitude(double longitude, double latitude) throws GeoLocationResolverException {
		log.info("resolving longitude {} and latitude {} coordinates", longitude, latitude);

		var country = DEFAULT_COUNTRY;
		var city = DEFAULT_CITY;
		var openStreetMapURL = "https://nominatim.openstreetmap.org/reverse.php?lon=" + longitude
				+ "&lat=" + latitude + "&format=jsonv2&accept-language=de";

		try {
			var addressDetails = new JSONObject(
					IOUtils.toString(new URI(openStreetMapURL), StandardCharsets.UTF_8));

			if (addressDetails.has("address")) {
				var addressJson = addressDetails.getJSONObject("address");
				country = addressJson.optString("country");
				city = addressJson.optString("city");
			}
		} catch (IOException | URISyntaxException e) {
			log.error("an exception occurred while resolving longitude {} and latitude {} coordinates", longitude, latitude, e);
			throw new GeoLocationResolverException(e);
		}

		return new LocationInfo(country, city);
	}
}
