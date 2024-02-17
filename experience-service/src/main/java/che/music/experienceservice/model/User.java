package che.music.experienceservice.model;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
	private String id;
	private String firstName;
	private String lastName;
	private String email;
}
