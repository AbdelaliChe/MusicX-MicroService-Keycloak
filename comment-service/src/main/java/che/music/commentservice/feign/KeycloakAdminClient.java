package che.music.commentservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "keycloakAdminClient", url = "${keycloak.admin.url}")
public interface KeycloakAdminClient {

	@GetMapping("/admin/realms/experience/users/{userId}")
	ResponseEntity<Map<String, Object>> getUserById(@PathVariable("userId") String userId,
	                                                @RequestHeader("Authorization") String authorizationHeader
	                                                );
}