package che.music.experienceservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@OpenAPIDefinition(info =
@Info(title = "Experience API", version = "${springdoc.version}", description = "Documentation Experience API v1.0")
)
public class ExperienceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExperienceServiceApplication.class, args);
	}

}