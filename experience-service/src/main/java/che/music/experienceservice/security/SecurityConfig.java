package che.music.experienceservice.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	JwtAuthConverter jwtAuthConverter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(cs->cs.disable());
		http.authorizeHttpRequests(auth->auth.anyRequest().authenticated());
		http.oauth2ResourceServer(o2r-> {
			o2r.jwt(
					jwtConfigurer ->
							jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter));
		});
		http.sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
}
