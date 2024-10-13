package che.music.experienceservice.web;

import che.music.experienceservice.dto.*;
import che.music.experienceservice.service.IExperienceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Provides flexible endpoints for experience_posts.
 *
 * @author a.chentoui
 * @version 1.0
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

	private final IExperienceService experienceService;

	/**
	 * createExperience
	 *
	 * @param experienceCreateDTO
	 * @return ExperienceReadDTO
	 */
	@PostMapping
	public ResponseEntity<ExperienceReadDTO> createExperience(@Valid @RequestBody ExperienceCreateDTO experienceCreateDTO){
		return ResponseEntity.status(HttpStatus.CREATED).body(experienceService.createExperience(experienceCreateDTO));
	}

	/**
	 * getExperienceById
	 *
	 * @param id experienceId
	 * @param fetchUser get user or not
	 * @return ExperienceReadDTO
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ExperienceReadDTO> getExperienceById(@PathVariable Long id,
	                                                           @RequestParam(value = "fetchUser", required = false, defaultValue = "false") boolean fetchUser){
		return ResponseEntity.ok(experienceService.getExperienceById(id, fetchUser));
	}

	/**
	 * getAllExperiences
	 *
	 * @param size
	 * @param page
	 * @param fetchUser
	 * @return list of ExperienceReadDTO
	 */
	@GetMapping
	public ResponseEntity<PaginatedResponse<ExperienceReadDTO>> getAllExperiences(
			@RequestParam(name = "page",defaultValue = "0") Integer page,
			@RequestParam(name = "size",defaultValue = "10") Integer size,
			@RequestParam(value = "fetchUser", required = false, defaultValue = "false") boolean fetchUser){
		return ResponseEntity.ok(experienceService.getAllExperiences(size, page,fetchUser));
	}

	/**
	 * getExperienceByItemId
	 *
	 * @param id itemId
	 * @param fetchUser get user or not
	 * @return List of ExperienceReadDTO
	 */
	@GetMapping("/item/{id}")
	public ResponseEntity<List<ExperienceReadDTO>> getExperienceByItemId(@PathVariable String id,
	                                                                     @RequestParam(value = "fetchUser", required = false, defaultValue = "false") boolean fetchUser){
		return ResponseEntity.ok(experienceService.getExperienceByItemId(id,fetchUser));
	}

	/**
	 * getExperienceByUserId
	 *
	 * @param userId itemId
	 * @param fetchUser get user or not
	 * @return List of ExperienceReadDTO
	 */
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<ExperienceReadDTO>> getExperienceByUserId(@PathVariable String userId,
	                                                                     @RequestParam(value = "fetchUser", required = false, defaultValue = "false") boolean fetchUser){
		return ResponseEntity.ok(experienceService.getExperienceByUserId(userId, fetchUser));
	}

	/**
	 * updateExperience
	 *
	 * @param id itemId
	 * @param experienceUpdateDTO
	 * @return ExperienceReadDTO
	 */
	@PutMapping("/{id}")
	public ResponseEntity<ExperienceReadDTO> updateExperience(@PathVariable Long id,
	                                               @RequestBody ExperienceUpdateDTO experienceUpdateDTO){
		return ResponseEntity.ok(experienceService.updateExperience(id,experienceUpdateDTO));
	}

	/**
	 * deleteExperience
	 *
	 * @param id itemId
	 * @return success message!
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteExperience(@PathVariable Long id){
		experienceService.deleteExperience(id);
		return ResponseEntity.ok("experience deleted!");
	}

	/**
	 * incrementExperienceViews
	 *
	 * @param id itemId
	 */
	@PatchMapping("/{id}/views")
	public ResponseEntity<Void> incrementExperienceViews(@PathVariable Long id) {
		experienceService.incrementViews(id);
		return ResponseEntity.noContent().build();
	}

	/**
	 * likeExperienceAction
	 *
	 * @param id itemId
	 */
	@PatchMapping("/{id}/likes")
	public ResponseEntity<Void> likeExperienceAction(@PathVariable Long id,
	                                                 @RequestParam(value = "like", defaultValue = "true") boolean like){
		experienceService.likeAction(id,like);
		return ResponseEntity.noContent().build();
	}

	/**
	 * archiveExperienceAction
	 *
	 * @param id itemId
	 * @param archive flag for archive/unarchive
	 */
	@PatchMapping("/{id}/archive")
	public ResponseEntity<Void> archiveExperienceAction(@PathVariable Long id,
	                                                    @RequestParam(value = "archive", defaultValue = "true") boolean archive){
		experienceService.archiveExperienceAction(id,archive);
		return ResponseEntity.noContent().build();
	}

}
