package che.music.commentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "EXPERIENCE-SERVICE")
public interface ExperienceClient {

	@GetMapping("/api/experiences/{experienceId}")
	ResponseEntity<?> getExperienceById(@PathVariable("experienceId") Long experienceId,
	                                         @RequestHeader("Authorization") String authorizationHeader);
}

