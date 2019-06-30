package domind.doc_management;
import domind.doc_management.property.FileStorageProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication

@EnableConfigurationProperties({
        FileStorageProperties.class
})

public class Doc_managementApplication {

	public static void main(String[] args) {
		SpringApplication.run(Doc_managementApplication.class, args);
	}

}
