package che.music.commentservice.service;

import che.music.commentservice.entity.Comment;
import java.util.List;
import java.util.Map;

public interface ICommentService {
	Comment createComment(Comment comment, String userId, Long experienceId);
	boolean updateComment(Comment comment, String userId);
	boolean deleteComment(Long id, String userId);
	Comment getCommentById(Long id, String jwt);
	List<Comment> getCommentByUserId(String userId);
	List<Comment> getAllComments(String Jwt);
	List<Comment> getAllCommentsOfExperience(Long experienceId, String jwt);
	List<Comment> getUserComment(Map<String,Object> user);
}

