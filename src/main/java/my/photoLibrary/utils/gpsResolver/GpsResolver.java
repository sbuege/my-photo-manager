package my.photoLibrary.utils.gpsResolver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.Strings;
import org.json.JSONObject;

@UtilityClass
@Log4j2
public class GpsResolver {

	private final static String DEFAULT_COUNTRY = Strings.EMPTY;
	private final static String DEFAULT_CITY = Strings.EMPTY;


	/**
	 * Resolves the given longitude and latitude coordinates to their corresponding country and city.
	 * <p>
	 * This method uses the OpenStreetMap Nominatim API to retrieve address details for the specified
	 * GPS coordinates. If the API response contains address information, the country and city are extracted.
	 * If an error occurs during the request or parsing of the response, an exception is thrown.
	 *
	 * @param longitude the longitude coordinate to resolve
	 * @param latitude  the latitude coordinate to resolve
	 * @return a {@link LocationInfo} record containing the resolved country and city
	 * @throws GpsResolverException if an error occurs while resolving the coordinates
	 */
	public static LocationInfo resolveLongitudeLatitude(double longitude, double latitude) throws
			GpsResolverException {
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
			throw new GpsResolverException(e);
		}

		var locationInfo = new LocationInfo(country, city);
		log.info("resolved location info: {}", locationInfo);

		return locationInfo;
	}
}
