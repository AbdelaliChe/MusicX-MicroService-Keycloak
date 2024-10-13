package che.music.commentservice.feign;


import che.music.commentservice.dto.TokenRequest;
import che.music.commentservice.dto.TokenResponse;
import che.music.commentservice.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth0", url = "https://dev-kat1rgca2l8mbhi3.us.auth0.com")
public interface Auth0Client {

	@PostMapping("/oauth/token")
	TokenResponse getAccessToken(@RequestBody TokenRequest tokenRequest);

	@GetMapping("/api/v2/users/{userId}")
	ResponseEntity<User> getUserById(@PathVariable("userId") String userId,
	                                 @RequestHeader("Authorization") String authorizationHeader);
}

