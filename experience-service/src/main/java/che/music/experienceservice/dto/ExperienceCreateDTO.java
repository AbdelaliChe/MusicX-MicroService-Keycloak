package che.music.experienceservice.dto;

import che.music.experienceservice.enums.AboutType;
import che.music.experienceservice.model.User;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ExperienceCreateDTO {
	private String name;
	private String imagePath;
	private AboutType about;
	private String description;
	private String userToughts;
	private String spotifyLink;
}
