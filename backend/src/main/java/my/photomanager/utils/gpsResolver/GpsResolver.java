package my.photomanager.utils.gpsResolver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
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

        if (longitude != 0.0 && latitude != 0.0) {

            var openStreetMapURL = "https://nominatim.openstreetmap.org/reverse.php?lon=" + longitude
                    + "&lat=" + latitude + "&format=jsonv2&accept-language=de";
            log.debug("OpenStreetMap Nominatim API URL: {}", openStreetMapURL);
            log.info("resolving longitude {} and latitude {} coordinates using OpenStreetMap Nominatim API", longitude, latitude);

            try {
                var httpClient = HttpClient.newBuilder()
                        .connectTimeout(Duration.ofSeconds(10))
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .build();

                var request = HttpRequest.newBuilder(new URI(openStreetMapURL))
                        .timeout(Duration.ofSeconds(20))
                        //.header("User-Agent", USER_AGENT)
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                var addressDetails = new JSONObject(response.body());


                if (addressDetails.has("address")) {
                    var addressJson = addressDetails.getJSONObject("address");
                    country = addressJson.optString("country");
                    city = addressJson.optString("city");
                }
            } catch (IOException | URISyntaxException | InterruptedException e) {
                log.error("an exception occurred while resolving longitude {} and latitude {} coordinates", longitude, latitude, e);
                throw new GpsResolverException(e);
            }
        }

        var locationInfo = new LocationInfo(country, city);
        log.info("resolved location info: {}", locationInfo);

        return locationInfo;
    }
}
