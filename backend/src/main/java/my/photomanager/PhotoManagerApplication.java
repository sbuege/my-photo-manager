package my.photomanager;

import my.photomanager.core.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhotoManagerApplication  {


    public static void main(String[] args) {
        SpringApplication.run(PhotoManagerApplication.class, args);
    }

}
