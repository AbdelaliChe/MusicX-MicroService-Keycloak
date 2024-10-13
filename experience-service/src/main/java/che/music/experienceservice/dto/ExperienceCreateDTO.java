package che.music.experienceservice.dto;

import che.music.experienceservice.enums.ItemType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ExperienceCreateDTO {

	private String spotifyItemId;
	private ItemType itemType;

	private String title;
	private String description;
	private String content;
}
