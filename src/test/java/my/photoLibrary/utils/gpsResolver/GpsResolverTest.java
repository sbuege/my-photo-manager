package my.photoLibrary.utils.gpsResolver;

import static my.photoLibrary.TestDataBuilder.TestPhoto001GPSLatitude;
import static my.photoLibrary.TestDataBuilder.TestPhoto001GPSLongitude;
import static my.photoLibrary.TestDataBuilder.TestPhoto001LocationCity;
import static my.photoLibrary.TestDataBuilder.TestPhoto001LocationCountry;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GpsResolverTest {

	@Test
	void shouldResolveLongitudeAndLatitude() throws GpsResolverException {
		// --- WHEN  ---
		var locationInfo = GpsResolver.resolveLongitudeLatitude(TestPhoto001GPSLongitude, TestPhoto001GPSLatitude);

		// --- THEN ---
		assertThat(locationInfo).isNotNull();
		assertThat(locationInfo.country()).isEqualTo(TestPhoto001LocationCountry);
		assertThat(locationInfo.city()).isEqualTo(TestPhoto001LocationCity);
	}
}