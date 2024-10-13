package che.music.commentservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRequest {
	private String grant_type;
	private String client_id;
	private String client_secret;
	private String audience;

}