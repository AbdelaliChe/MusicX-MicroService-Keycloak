package che.music.commentservice.service;

import che.music.commentservice.entity.Comment;
import che.music.commentservice.exception.NotFoundException;
import che.music.commentservice.exception.NotYoursException;
import che.music.commentservice.feign.KeycloakAdminClient;
import che.music.commentservice.model.User;
import che.music.commentservice.repository.ICommentDao;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class CommentServiceImp implements ICommentService {

	private ICommentDao commentDao;
	private KeycloakAdminClient keycloakAdminClient;

	@Override
	public Comment createComment(Comment comment, String userId, Long experienceId) {
		comment.setUserId(userId);
		comment.setExperienceId(experienceId);
		return commentDao.save(comment);
	}

	@Override
	public boolean updateComment(Comment comment, String userId) {
		Comment commentToEdit = getCommentById(comment.getId());
		if (commentToEdit!=null) {
			if (!commentToEdit.getUserId().equals(userId)) {
				throw new NotFoundException("you can't update a comment that you don't own");
			}
			commentToEdit.setText(comment.getText());
			commentDao.save(commentToEdit);
			return true;
		} else {
			throw new NotFoundException("Comment not found with id: " + comment.getId());
		}
	}

	@Override
	public boolean deleteComment(Long id, String userId) {
		Comment comment = getCommentById(id);
		if(comment != null){
			if(comment.getUserId().equals(userId)){
				commentDao.delete(comment);
				return true;
			}else{
				throw new NotYoursException("you can't delete a comment that you don't own");
			}
		}else{
			throw new NotFoundException("Comment not found with id: " + id);
		}
	}

	@Override
	public Comment getCommentById(Long id, String jwt) {
		Comment comment = getCommentById(id);
		if(comment != null){
			Map<String, Object> response =
					keycloakAdminClient.getUserById(comment.getUserId(),jwt).getBody();
			User user = User.builder()
					.id(comment.getUserId())
					.firstName((String) response.get("firstName"))
					.lastName((String) response.get("lastName"))
					.email((String) response.get("email"))
					.build();
			comment.setUser(user);
			return comment;
		}else{
			throw new NotFoundException("Comment not found with id: " + id);
		}
	}

	@Override
	public List<Comment> getCommentByUserId(String userId) {
		return commentDao.findByUserId(userId);
	}

	@Override
	public List<Comment> getAllComments(String jwt) {
		List<Comment> comments = commentDao.findAll();
		List<Comment> allCommentWithUsers = comments.stream().map(comment -> {
			Map<String, Object> response =
					keycloakAdminClient.getUserById(comment.getUserId(),jwt).getBody();
			User user = User.builder()
					.id(comment.getUserId())
					.firstName((String) response.get("firstName"))
					.lastName((String) response.get("lastName"))
					.email((String) response.get("email"))
					.build();
			comment.setUser(user);
			return comment;
		}).collect(Collectors.toList());
		return allCommentWithUsers;
	}

	@Override
	public List<Comment> getAllCommentsOfExperience(Long experienceId, String jwt) {
		List<Comment> comments = commentDao.findCommentsByExperienceId(experienceId);
		List<Comment> allCommentWithUsers = comments.stream().map(comment -> {
			Map<String, Object> response =
					keycloakAdminClient.getUserById(comment.getUserId(),jwt).getBody();
			User user = User.builder()
					.id(comment.getUserId())
					.firstName((String) response.get("firstName"))
					.lastName((String) response.get("lastName"))
					.email((String) response.get("email"))
					.build();
			comment.setUser(user);
			return comment;
		}).collect(Collectors.toList());
		return allCommentWithUsers;
	}

	@Override
	public List<Comment> getUserComment(Map<String, Object> userMap) {
		List<Comment> comments = commentDao.findByUserId((String) userMap.get("sub"));
		List<Comment> allCommentForUser = comments.stream().map(comment -> {
			User user = User.builder()
					.id(comment.getUserId())
					.firstName((String) userMap.get("given_name"))
					.lastName((String) userMap.get("family_name"))
					.email((String) userMap.get("email"))
					.build();
			comment.setUser(user);
			return comment;
		}).collect(Collectors.toList());
		return allCommentForUser;
	}
	public Comment getCommentById(Long id) {
		return commentDao.findById(id).orElse(null);
	}
}
