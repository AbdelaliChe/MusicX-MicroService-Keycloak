package che.music.commentservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommentCreateDTO {
	private Long experienceId;
	private String content;
}
