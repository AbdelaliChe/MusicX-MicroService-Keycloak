package che.music.experienceservice.feign;

import che.music.experienceservice.dto.Comment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "COMMENT-SERVICE")
public interface CommentClient {

	@GetMapping("/api/comment/experience/{experienceId}")
	ResponseEntity<List<Comment>> getAllCommentsOfExperience(@PathVariable("experienceId") Long experienceId,
	                                                         @RequestHeader("Authorization") String authorizationHeader
	                                                );
}

