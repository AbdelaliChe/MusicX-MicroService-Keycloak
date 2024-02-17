package che.music.experienceservice.service;

import che.music.experienceservice.entity.Experience;

import java.util.List;
import java.util.Map;

public interface IExperienceService {
	Experience createExperience(Experience experience, String userId);
	boolean updateExperience(Experience experience, String userId);
	boolean deleteExperience(Long id, String userId);
	Experience getExperienceById(Long id, String jwt);
	Experience getExperienceByName(String name);
	List<Experience> getExperienceByUserId(String userId);
	List<Experience> getAllExperiences(String Jwt);
	List<Experience> getUserExperience(Map<String,Object> user);
}

