package che.music.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestAttributeHandler;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	@Value("${okta.oauth2.audience}")
	private String audience;

	private final ReactiveClientRegistrationRepository clientRegistrationRepository;

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		http.csrf(csrf -> csrf.disable());
		http.authorizeExchange(auth->auth
				.pathMatchers("/actuator/**","/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
				.anyExchange().authenticated());
		http.oauth2Login(oauth2 -> oauth2
				.authorizationRequestResolver(
						authorizationRequestResolver(this.clientRegistrationRepository)
				)
		);
		return http.build();
	}

	private ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver(
			ReactiveClientRegistrationRepository clientRegistrationRepository) {

		DefaultServerOAuth2AuthorizationRequestResolver authorizationRequestResolver =
				new DefaultServerOAuth2AuthorizationRequestResolver(
						clientRegistrationRepository);
		authorizationRequestResolver.setAuthorizationRequestCustomizer(
				authorizationRequestCustomizer());

		return authorizationRequestResolver;
	}

	private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer() {
		return customizer -> customizer
				.additionalParameters(params -> params.put("audience", audience));
	}
}