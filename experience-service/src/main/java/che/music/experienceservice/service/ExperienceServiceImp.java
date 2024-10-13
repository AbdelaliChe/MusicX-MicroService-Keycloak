package che.music.experienceservice.service;

import che.music.experienceservice.dto.*;
import che.music.experienceservice.entity.Experience;
import che.music.experienceservice.feign.Auth0Client;
import che.music.experienceservice.mapper.ExperienceDtoConverter;
import che.music.experienceservice.repository.IExperienceRepo;
import che.music.experienceservice.exception.NotFoundException;
import che.music.experienceservice.exception.NotYoursException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExperienceServiceImp implements IExperienceService{

	private final IExperienceRepo experienceRepo;
	private final Auth0Client auth0Client;
	private final ExperienceDtoConverter experienceDtoConverter;

	@Value("${okta.oauth2.clientId}")
	private String clientId;

	@Value("${okta.oauth2.clientSecret}")
	private String clientSecret;

	@Value("${okta.oauth2.audience}")
	private String audience;

	@Transactional
	@Override
	public ExperienceReadDTO createExperience(ExperienceCreateDTO experienceDto) {
		Experience experience = experienceDtoConverter.convertToEntity(experienceDto);
		String userId = (String) this.getCurrentUser().get("sub");
		experience.setUserId(userId);
		ExperienceReadDTO experienceReadDTO = experienceDtoConverter.convertToReadDTO(
						experienceRepo.save(experience));
		return experienceReadDTO;
	}

	@Override
	public ExperienceReadDTO getExperienceById(Long id, boolean fetchUser) {
		Experience experience = experienceRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Experience not found with id: " + id));
		return this.getExperienceReadDTO(fetchUser, experience);
	}

	@Override
	public List<ExperienceReadDTO> getExperienceByItemId(String id, boolean fetchUser) {
		List<Experience> experiences = experienceRepo.findExperienceBySpotifyItemId(id);
		return experiences.stream().map(experience ->
				this.getExperienceReadDTO(fetchUser, experience))
				.collect(Collectors.toList());
	}

	@Override
	public PaginatedResponse<ExperienceReadDTO> getAllExperiences(Integer size, Integer page, boolean fetchUser) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Experience> experiences = experienceRepo.findAllNonArchived(pageable);

		List<ExperienceReadDTO> experienceReadDTOList = experiences.stream().map(experience ->
						this.getExperienceReadDTO(fetchUser, experience))
				.collect(Collectors.toList());

		return PaginatedResponse.<ExperienceReadDTO>builder()
				.content(experienceReadDTOList)
				.pageNumber(experiences.getNumber())
				.pageSize(experiences.getSize())
				.totalElements(experiences.getTotalElements())
				.totalPages(experiences.getTotalPages())
				.build();
	}

	@Override
	public List<ExperienceReadDTO> getExperienceByUserId(String userId, boolean fetchUser) {
		List<Experience> experiences = experienceRepo.findByUserId(userId);
		return experiences.stream().map(experience ->
						this.getExperienceReadDTO(fetchUser, experience))
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public ExperienceReadDTO updateExperience(Long id, ExperienceUpdateDTO experienceUpdateDTO) {
		// get xp to edit
		Experience experienceToEdit = experienceRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Experience not found with id: " + id));
		// sent experience
		Experience experience = experienceDtoConverter.convertToEntity(experienceUpdateDTO);

		String userId = (String) getCurrentUser().get("sub");
		// check for owner
		if(experienceToEdit.getUserId().equals(userId)){
			if (experience.getContent() != null && !experience.getContent().isEmpty()) {
				experienceToEdit.setContent(experience.getContent());
			}
			if (experience.getTitle() != null && !experience.getTitle().isEmpty()) {
				experienceToEdit.setTitle(experience.getTitle());
			}
			if (experience.getDescription() != null && !experience.getDescription().isEmpty()) {
				experienceToEdit.setDescription(experience.getDescription());
			}
			experienceToEdit.setUpdatedAt( LocalDateTime.now());
			return experienceDtoConverter.convertToReadDTO(experienceRepo.save(experienceToEdit));
		}else{
			throw new NotYoursException("You can't update experience with id: " + id + " because you don't own it.");
		}
	}

	@Transactional
	@Override
	public boolean deleteExperience(Long id) {
		// get xp to delete
		Experience experience = experienceRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Experience not found with id: " + id));
		// maybe just archive it ?
		String userId = (String) getCurrentUser().get("sub");
		// check for ownership
		if(experience.getUserId().equals(userId)){
			experienceRepo.delete(experience);
			return true;
		}else{
			throw new NotYoursException("You can't delete experience with id: " + id + " because you don't own it.");
		}
	}

	@Override
	public void incrementViews(Long id) {
		Experience experience = experienceRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Experience not found with id: " + id));
		experience.setViews(experience.getViews()+1);
		experienceRepo.save(experience);
	}

	@Override
	public void likeAction(Long id, boolean like) {
		Experience experience = experienceRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Experience not found with id: " + id));
		if (like) {
			experience.setLikes(experience.getLikes() + 1);
		} else {
			experience.setLikes(experience.getLikes() - 1);
		}
		experienceRepo.save(experience);
	}

	@Override
	public void archiveExperienceAction(Long id, boolean archive) {
		Experience experience = experienceRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Experience not found with id: " + id));
		experience.setArchived(archive);
		experience.setUpdatedAt( LocalDateTime.now());
		experienceRepo.save(experience);
	}

	//commons
	private ExperienceReadDTO getExperienceReadDTO(boolean fetchUser, Experience experience) {
		ExperienceReadDTO experienceReadDTO = experienceDtoConverter.convertToReadDTO(experience);
		if(fetchUser) experienceReadDTO.setUser(this.buildUser(experience.getUserId()));
		return experienceReadDTO;
	}

	private User buildUser(String userId) {
		String token = this.getToken();
		String authorizationHeader = "Bearer " + token;
		ResponseEntity<User> response = auth0Client.getUserById(userId, authorizationHeader);
		log.info("auth0 response {}", response);
		if(response.getStatusCode().equals(HttpStatus.OK)) return response.getBody();
		throw new NotFoundException("User with id : "+ userId +" doesn't exit!");
	}

	private String getToken(){
		TokenRequest tokenRequest = TokenRequest.builder()
				.grant_type("client_credentials")
				.client_id(clientId)
				.client_secret(clientSecret)
				.audience(audience)
				.build();
		return auth0Client.getAccessToken(tokenRequest).getAccess_token();
	}

	private Map<String, Object> getCurrentUser(){
		OAuth2User oAuth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return oAuth2User.getAttributes();
	}
}
