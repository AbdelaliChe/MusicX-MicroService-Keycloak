package che.music.experienceservice.dto;

import che.music.experienceservice.enums.ItemType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ExperienceUpdateDTO{
	private String title;
	private String description;
	private String content;
}
