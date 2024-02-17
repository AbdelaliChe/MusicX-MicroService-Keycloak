package che.music.experienceservice.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
	private Long id;
	private String text;
	private User user;
}
