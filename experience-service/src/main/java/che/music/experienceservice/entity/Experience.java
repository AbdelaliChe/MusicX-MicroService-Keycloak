package che.music.experienceservice.entity;

import che.music.experienceservice.enums.AboutType;
import che.music.experienceservice.model.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "experience")
@Entity
public class Experience {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String imagePath;
	@Enumerated(EnumType.STRING)
	private AboutType about; // - about (song-album-artist)
	private String description;
	private String userToughts;
	private String spotifyLink;
	private boolean archived;

	@Transient
	private User user;

	private String userId;
}
