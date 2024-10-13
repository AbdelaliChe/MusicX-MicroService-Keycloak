package che.music.commentservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@OpenAPIDefinition(info =
@Info(title = "Comment API", version = "${springdoc.version}", description = "Documentation Comment API v1.0")
)
public class CommentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentServiceApplication.class, args);
	}

}
