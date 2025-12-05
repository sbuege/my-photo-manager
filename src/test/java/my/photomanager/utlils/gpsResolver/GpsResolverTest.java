package my.photomanager.utlils.gpsResolver;

import static my.photomanager.TestDataBuilder.EXAMPLE_001_CITY;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_COUNTRY;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_LATITUDE;
import static my.photomanager.TestDataBuilder.EXAMPLE_001_LONGITUDE;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.log4j.Log4j2;
import my.photomanager.utils.gpsResolver.GpsResolver;
import my.photomanager.utils.gpsResolver.GpsResolverException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class GpsResolverTest {

	@Test
	@DisplayName("should resolve longitude and latitude to location information")
	void shouldResolveLongitudeAndLatitude() throws GpsResolverException {
		// --- WHEN  ---
		var locationInfo = GpsResolver.resolveLongitudeLatitude(EXAMPLE_001_LONGITUDE, EXAMPLE_001_LATITUDE);

		// --- THEN ---
		assertThat(locationInfo).isNotNull();
		assertThat(locationInfo.country()).isEqualTo(EXAMPLE_001_COUNTRY);
		assertThat(locationInfo.city()).isEqualTo(EXAMPLE_001_CITY);
	}
}