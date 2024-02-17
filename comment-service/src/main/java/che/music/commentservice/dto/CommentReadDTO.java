package che.music.commentservice.dto;

import che.music.commentservice.model.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommentReadDTO {
	private Long id;
	private String text;
	private Long experienceId;
	private User user;
}
