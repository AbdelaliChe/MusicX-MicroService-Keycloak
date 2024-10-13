package che.music.commentservice.repository;

import che.music.commentservice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepo extends JpaRepository<Comment,Long> {
	List<Comment> findByUserId(String userId);
	List<Comment> findCommentsByExperienceId(Long ExperienceId);
}
