package my.photomanager.geoLocationResolver;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
class GeoLocationResolverTest {

	@Test
	void shouldResolveLongitudeAndLatitude() throws GeoLocationResolverException {
		// when
		var locationInfo = GeoLocationResolver.resolveLongitudeLatitude(13.376194444444446, 52.518680555555555);
		log.info("location info: {}", locationInfo);

		// then
		assertThat(locationInfo).isNotNull();
		assertThat(locationInfo.country()).isEqualTo("Deutschland");
		assertThat(locationInfo.city()).isEqualTo("Berlin");
	}
}