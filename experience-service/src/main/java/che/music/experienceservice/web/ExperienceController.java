package che.music.experienceservice.web;

import che.music.experienceservice.dto.*;
import che.music.experienceservice.entity.Experience;
import che.music.experienceservice.service.IExperienceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

	private IExperienceService experienceService;
	private ExperienceDtoConverter experienceDtoConverter;

	@GetMapping("/{id}")
	public ResponseEntity<ExperienceReadDTO> getExperience(@PathVariable Long id,
	                                         @RequestHeader("Authorization") String authorizationHeader){
		Experience experience = experienceService.getExperienceById(id, authorizationHeader);
		ExperienceReadDTO experienceReadDTO = experienceDtoConverter
				.convertToReadDTO(experience);
		return ResponseEntity.ok(experienceReadDTO);
	}

	@GetMapping("/mine")
	public ResponseEntity<List<ExperienceReadDTO>> getUserExperiences(){
		List<Experience> experiences = experienceService.getUserExperience(getCurrentUser());
		List<ExperienceReadDTO> experienceReadDTOS = experiences.stream().map(experience ->
				experienceDtoConverter.convertToReadDTO(experience))
				.collect(Collectors.toList());
		return ResponseEntity.ok(experienceReadDTOS);
	}

	@GetMapping
	public ResponseEntity<List<ExperienceReadDTO>> getAllExperience(@RequestHeader("Authorization") String authorizationHeader){
		List<Experience> experiences = experienceService.getAllExperiences(authorizationHeader);
		List<ExperienceReadDTO> experienceReadDTOS = experiences.stream().map(experience ->
				experienceDtoConverter.convertToReadDTO(experience))
				.collect(Collectors.toList());
		return ResponseEntity.ok(experienceReadDTOS);
	}

	@PostMapping
	public ResponseEntity<Experience> createExperience(@RequestBody ExperienceCreateDTO experienceCreateDTO){
		String userId = (String) getCurrentUser().get("sub");
		Experience experience = experienceDtoConverter.convertToEntity(experienceCreateDTO);
		return ResponseEntity.ok(experienceService.createExperience(experience,userId));
	}

	@PutMapping
	public ResponseEntity<String> updateExperience(@RequestBody ExperienceUpdateDTO experienceUpdateDTO){
		String userId = (String) getCurrentUser().get("sub");
		Experience experience = experienceDtoConverter.convertToEntity(experienceUpdateDTO);
		experienceService.updateExperience(experience,userId);
		return ResponseEntity.ok("updated!!!");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteExperience(@PathVariable Long id){
		String userId = (String) getCurrentUser().get("sub");
		experienceService.deleteExperience(id,userId);
		return ResponseEntity.ok("deleted!!!");
	}

	private Map<String, Object> getCurrentUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
		Map<String, Object> userClaims = jwtToken.getTokenAttributes();
		return userClaims;
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<String> user(){
		return ResponseEntity.ok("hello user!!!");
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> admin(){
		return ResponseEntity.ok("Hi admin!!!");
	}
}
