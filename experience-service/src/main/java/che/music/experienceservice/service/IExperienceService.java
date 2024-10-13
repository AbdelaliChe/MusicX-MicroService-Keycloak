package che.music.experienceservice.service;

import che.music.experienceservice.dto.ExperienceCreateDTO;
import che.music.experienceservice.dto.ExperienceReadDTO;
import che.music.experienceservice.dto.ExperienceUpdateDTO;
import che.music.experienceservice.dto.PaginatedResponse;

import java.util.List;

/**
 * IExperienceService
 *
 * <p>
 * service interface for interacting with Experiences
 * </p>
 *
 */
public interface IExperienceService {
	/**
	 * createExperience
	 *
	 * @param experienceCreateDTO
	 * @return ExperienceReadDTO
	 */
	ExperienceReadDTO createExperience(ExperienceCreateDTO experienceCreateDTO);

	/**
	 * getExperienceById
	 *
	 * @param id        experienceId
	 * @param fetchUser
	 * @return ExperienceReadDTO
	 */
	ExperienceReadDTO getExperienceById(Long id, boolean fetchUser);

	/**
	 * getExperienceByItemId
	 *
	 * @param id        itemId
	 * @param fetchUser
	 * @return List of ExperienceReadDTO
	 */
	List<ExperienceReadDTO> getExperienceByItemId(String id, boolean fetchUser);

	/**
	 * getAllExperiences
	 *
	 * @param size
	 * @param page
	 * @param fetchUser
	 * @return list of ExperienceReadDTO
	 */
	PaginatedResponse<ExperienceReadDTO> getAllExperiences(Integer size, Integer page, boolean fetchUser);

	/**
	 * getExperienceByUserId
	 *
	 * @param userId
	 * @param fetchUser
	 * @return List of ExperienceReadDTO
	 */
	List<ExperienceReadDTO> getExperienceByUserId(String userId, boolean fetchUser);

	/**
	 * updateExperience
	 *
	 * @param id itemId
	 * @param experienceUpdateDTO
	 * @return ExperienceReadDTO
	 */
	ExperienceReadDTO updateExperience(Long id, ExperienceUpdateDTO experienceUpdateDTO);

	/**
	 * deleteExperience
	 *
	 * @param id itemId
	 * @return boolean for delete/not
	 */
	boolean deleteExperience(Long id);

	/**
	 * incrementExperienceViews
	 *
	 * @param id itemId
	 */
	void incrementViews(Long id);

	/**
	 * likeExperienceAction
	 *
	 * @param id itemId
	 */
	void likeAction(Long id, boolean like);

	/**
	 * archiveExperienceAction
	 *
	 * @param id itemId
	 * @param archive flag for archive/unarchive
	 */
	void archiveExperienceAction(Long id, boolean archive);
}

