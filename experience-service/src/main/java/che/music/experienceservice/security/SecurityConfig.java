package che.music.experienceservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	@Value("${okta.oauth2.audience}")
	private String audience;

	private final ClientRegistrationRepository clientRegistrationRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(auth-> {
			auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();
			auth.anyRequest().authenticated();
		});
		http.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(authorization -> authorization
						.authorizationRequestResolver(
								authorizationRequestResolver(this.clientRegistrationRepository)
						)
				)
		);

		return http.build();
	}

	private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
			ClientRegistrationRepository clientRegistrationRepository) {

		DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
				new DefaultOAuth2AuthorizationRequestResolver(
						clientRegistrationRepository, "/oauth2/authorization");
		authorizationRequestResolver.setAuthorizationRequestCustomizer(
				authorizationRequestCustomizer());

		return authorizationRequestResolver;
	}

	private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
		return customizer -> customizer
				.additionalParameters(params -> params.put("audience", audience));
	}
}
