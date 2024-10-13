package che.music.gatewayservice.web;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@GetMapping("/hello")
	public String helloUser(@AuthenticationPrincipal OAuth2User user) {
		return "Hello, " + user.getName();
	}

	@GetMapping("/print-token")
	public String printAccessToken(@RegisteredOAuth2AuthorizedClient("okta")
	                               OAuth2AuthorizedClient authorizedClient) {
		var accessToken = authorizedClient.getAccessToken();
		logger.info("Access Token Value: {}",accessToken.getTokenValue());
		return "Access token printed";
	}

}