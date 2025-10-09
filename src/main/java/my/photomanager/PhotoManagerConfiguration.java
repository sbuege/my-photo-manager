package my.photomanager;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class PhotoManagerConfiguration {

	@Value("${photo.sourceFolder:photos}")
	private String photoSourceFolder;
}
