package che.music.experienceservice.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Comment {
	private Long id;
	private Long experienceId;
	private String userId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
