package che.music.experienceservice.dto;

import che.music.experienceservice.enums.AboutType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ExperienceUpdateDTO{
	private Long id;
	private String name;
	private String imagePath;
	private AboutType about;
	private String description;
	private String userToughts;
	private String spotifyLink;
	private boolean archived;
}
