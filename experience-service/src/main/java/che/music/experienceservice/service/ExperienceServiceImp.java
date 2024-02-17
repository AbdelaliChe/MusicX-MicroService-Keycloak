package che.music.experienceservice.service;

import che.music.experienceservice.entity.Experience;
import che.music.experienceservice.feign.KeycloakAdminClient;
import che.music.experienceservice.model.User;
import che.music.experienceservice.repository.IExperienceDao;
import che.music.experienceservice.exception.NotFoundException;
import che.music.experienceservice.exception.NotYoursException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class ExperienceServiceImp implements IExperienceService{

	private IExperienceDao experienceDao;
	private KeycloakAdminClient keycloakAdminClient;

	@Override
	public Experience createExperience(Experience experience, String userId) {
		experience.setUserId(userId);
		return experienceDao.save(experience);
	}

	@Override
	public boolean updateExperience(Experience experience, String userId) {
		Experience experienceToEdit = getExperienceById(experience.getId());
		if (experienceToEdit!=null) {

			if (!experienceToEdit.getUserId().equals(userId)) {
				throw new NotYoursException("you can't update an experience that you don't own");
			}

			if (experience.getUserToughts() != null && !experience.getUserToughts().isEmpty()) {
				experienceToEdit.setUserToughts(experience.getUserToughts());
			}
			if (experience.getUserId() != null && !experience.getUserId().isEmpty()) {
				experienceToEdit.setUserId(experience.getUserId());
			}
			if (experience.getAbout() != null) {
				experienceToEdit.setAbout(experience.getAbout());
			}
			if (experience.getName() != null && !experience.getName().isEmpty()) {
				experienceToEdit.setName(experience.getName());
			}
			if (experience.getDescription() != null && !experience.getDescription().isEmpty()) {
				experienceToEdit.setDescription(experience.getDescription());
			}
			if (experience.getImagePath() != null && !experience.getImagePath().isEmpty()) {
				experienceToEdit.setImagePath(experience.getImagePath());
			}
			if (experience.getSpotifyLink() != null && !experience.getSpotifyLink().isEmpty()) {
				experienceToEdit.setSpotifyLink(experience.getSpotifyLink());
			}

			experienceDao.save(experienceToEdit);
			return true;
		} else {
			throw new NotFoundException("Experience not found with id: " + experience.getId());
		}
	}


	private Experience getExperienceById(Long id) {
		return experienceDao.findById(id).orElse(null);
	}
	@Override
	public boolean deleteExperience(Long id, String userId) {
		Experience experience = getExperienceById(id);
		if(experience != null){
			if(experience.getUserId().equals(userId)){
				experienceDao.delete(experience);
				return true;
			}else{
				throw new NotYoursException("you can't delete an experience that you don't own");
			}
		}else{
			throw new NotFoundException("Experience not found with id: " + id);
		}
	}

	@Override
	public Experience getExperienceById(Long id, String jwt) {
		//this jwt is with Bearer
		Experience experience = getExperienceById(id);
		if(experience != null){
			Map<String, Object> response =
					keycloakAdminClient.getUserById(experience.getUserId(),jwt).getBody();
			User user = User.builder()
					.id(experience.getUserId())
					.firstName((String) response.get("firstName"))
					.lastName((String) response.get("lastName"))
					.email((String) response.get("email"))
					.build();
			experience.setUser(user);
			return experience;
		}else{
			throw new NotFoundException("Experience not found with id: " + id);
		}
	}

	@Override
	public Experience getExperienceByName(String name) {
		return experienceDao.findByName(name);
	}

	@Override
	public List<Experience> getExperienceByUserId(String userId) {
		return experienceDao.findByUserId(userId);
	}

	@Override
	public List<Experience> getAllExperiences(String jwt) {
		List<Experience> experiences = experienceDao.findAll();
		List<Experience> allExperienceWithUsers = experiences.stream().map(experience -> {
			Map<String, Object> response =
					keycloakAdminClient.getUserById(experience.getUserId(),jwt).getBody();
			User user = User.builder()
					.id(experience.getUserId())
					.firstName((String) response.get("firstName"))
					.lastName((String) response.get("lastName"))
					.email((String) response.get("email"))
					.build();
			experience.setUser(user);
			return experience;
		}).collect(Collectors.toList());
		return allExperienceWithUsers;
	}

	@Override
	public List<Experience> getUserExperience(Map<String,Object> userMap) {
		List<Experience> experiences = experienceDao.findByUserId((String) userMap.get("sub"));
		List<Experience> allExperienceForUser = experiences.stream().map(experience -> {
			User user = User.builder()
					.id(experience.getUserId())
					.firstName((String) userMap.get("given_name"))
					.lastName((String) userMap.get("family_name"))
					.email((String) userMap.get("email"))
					.build();
			experience.setUser(user);
			return experience;
		}).collect(Collectors.toList());
		return allExperienceForUser;
	}
}
