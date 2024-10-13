package che.music.commentservice.web;

import che.music.commentservice.dto.CommentCreateDTO;
import che.music.commentservice.dto.CommentReadDTO;
import che.music.commentservice.dto.CommentUpdateDTO;
import che.music.commentservice.service.ICommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Provides flexible endpoints for experience_comment.
 *
 * @author a.chentoui
 * @version 1.0
 */
@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/api/comments")
public class CommentController {

	private final ICommentService commentService;

	/**
	 * createComment
	 *
	 * @param commentCreateDTO
	 * @return CommentReadDTO
	 */
	@PostMapping
	public ResponseEntity<CommentReadDTO> createComment(@RequestBody CommentCreateDTO commentCreateDTO){
		return ResponseEntity.ok(commentService.createComment(commentCreateDTO));
	}

	/**
	 * updateComment
	 *
	 * @param id commentID
	 * @param commentUpdateDTO
	 * @return CommentReadDTO
	 */
	@PutMapping("/{id}")
	public ResponseEntity<CommentReadDTO> updateComment(@PathVariable Long id, @RequestBody CommentUpdateDTO commentUpdateDTO){
		return ResponseEntity.ok(commentService.updateComment(id,commentUpdateDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable Long id){
		commentService.deleteComment(id);
		return ResponseEntity.ok("deleted!!!");
	}

	/**
	 * getCommentsByExperienceId
	 *
	 * @param experienceId
	 * @return List of CommentReadDTO
	 */
	@GetMapping("/experience/{experienceId}")
	public ResponseEntity<List<CommentReadDTO>> getCommentsByExperienceId(@PathVariable Long experienceId){
		return ResponseEntity.ok(commentService.getCommentsByExperienceId(experienceId));
	}
}
