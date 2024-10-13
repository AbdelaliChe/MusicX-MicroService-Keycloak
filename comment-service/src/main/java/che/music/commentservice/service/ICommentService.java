package che.music.commentservice.service;

import che.music.commentservice.dto.CommentCreateDTO;
import che.music.commentservice.dto.CommentReadDTO;
import che.music.commentservice.dto.CommentUpdateDTO;

import java.util.List;

/**
 * ICommentService
 *
 * <p>
 * service interface for interacting with Experiences comments
 * </p>
 *
 */
public interface ICommentService {

	/**
	 * createComment
	 *
	 * @param commentCreateDTO
	 * @return CommentReadDTO
	 */
	CommentReadDTO createComment(CommentCreateDTO commentCreateDTO);

	/**
	 * updateComment
	 *
	 * @param id itemId
	 * @param commentUpdateDTO
	 * @return CommentReadDTO
	 */
	CommentReadDTO updateComment(Long id, CommentUpdateDTO commentUpdateDTO);

	/**
	 * deleteExperience
	 *
	 * @param id itemId
	 * @return boolean for delete/not
	 */
	boolean deleteComment(Long id);

	/**
	 * getExperienceById
	 *
	 * @param experienceId
	 * @return CommentReadDTO
	 */
	List<CommentReadDTO> getCommentsByExperienceId(Long experienceId);
}

