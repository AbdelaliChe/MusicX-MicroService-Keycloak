package che.music.commentservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommentUpdateDTO {
	private String content;
}
