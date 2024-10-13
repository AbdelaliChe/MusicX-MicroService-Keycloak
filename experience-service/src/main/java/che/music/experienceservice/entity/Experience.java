package che.music.experienceservice.entity;

import che.music.experienceservice.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
	private String spotifyItemId;
	private String userId;

	@Enumerated(EnumType.STRING)
	private ItemType itemType; // type of item (song-album-artist)

	private String title;
	private String description;
	private String content;

	private boolean archived = false;
	private int views = 0;
	private int likes = 0;
	@Column(updatable = false)
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
