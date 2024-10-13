package che.music.experienceservice.dto;

import che.music.experienceservice.enums.ItemType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ExperienceReadDTO {
	private Long id;
	private String spotifyItemId;
	private String userId;
	private ItemType itemType;

	private String title;
	private String description;
	private String content;

	private boolean archived;
	private int views;
	private int likes;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private User user;
}
