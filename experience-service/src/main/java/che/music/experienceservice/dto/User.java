package che.music.experienceservice.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
	private String user_id;
	private String nickname;
	private String picture;
	private String name;
	private String email;
}
