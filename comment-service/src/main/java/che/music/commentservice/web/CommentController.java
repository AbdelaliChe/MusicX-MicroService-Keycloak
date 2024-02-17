package che.music.commentservice.web;

import che.music.commentservice.dto.CommentCreateDTO;
import che.music.commentservice.dto.CommentDtoConverter;
import che.music.commentservice.dto.CommentReadDTO;
import che.music.commentservice.dto.CommentUpdateDTO;
import che.music.commentservice.entity.Comment;
import che.music.commentservice.service.ICommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

	private ICommentService commentService;
	private CommentDtoConverter commentDtoConverter;

	@GetMapping("/{id}")
	public ResponseEntity<CommentReadDTO> getComment(@PathVariable Long id,
	                                                 @RequestHeader("Authorization") String authorizationHeader){
		Comment comment = commentService.getCommentById(id, authorizationHeader);
		CommentReadDTO commentReadDTO = commentDtoConverter.convertToReadDTO(comment);
		return ResponseEntity.ok(commentReadDTO);
	}

	@GetMapping("/mine")
	public ResponseEntity<List<CommentReadDTO>> getUserComments(){
		List<Comment> comments = commentService.getUserComment(getCurrentUser());
		List<CommentReadDTO> commentReadDTOS = comments.stream().map(comment ->
				commentDtoConverter.convertToReadDTO(comment))
				.collect(Collectors.toList());
		return ResponseEntity.ok(commentReadDTOS);
	}

	@GetMapping
	public ResponseEntity<List<CommentReadDTO>> getAllComment(@RequestHeader("Authorization") String authorizationHeader){
		List<Comment> comments = commentService.getAllComments(authorizationHeader);
		List<CommentReadDTO> commentReadDTOS = comments.stream().map(comment ->
				commentDtoConverter.convertToReadDTO(comment))
				.collect(Collectors.toList());
		return ResponseEntity.ok(commentReadDTOS);
	}

	@PostMapping("/{experienceId}")
	public ResponseEntity<Comment> createComment(@PathVariable Long experienceId, @RequestBody CommentCreateDTO commentCreateDTO){
		String userId = (String) getCurrentUser().get("sub");
		Comment comment = commentDtoConverter.convertToEntity(commentCreateDTO);
		return ResponseEntity.ok(commentService.createComment(comment,userId, experienceId));
	}

	@PutMapping
	public ResponseEntity<String> updateComment(@RequestBody CommentUpdateDTO commentUpdateDTO){
		String userId = (String) getCurrentUser().get("sub");
		Comment comment = commentDtoConverter.convertToEntity(commentUpdateDTO);
		commentService.updateComment(comment,userId);
		return ResponseEntity.ok("updated!!!");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteComment(@PathVariable Long id){
		String userId = (String) getCurrentUser().get("sub");
		commentService.deleteComment(id,userId);
		return ResponseEntity.ok("deleted!!!");
	}

	@GetMapping("experience/{experienceId}")
	public ResponseEntity<List<Comment>> getAllCommentsOfExperience(@PathVariable Long experienceId,
	                                     @RequestHeader("Authorization") String authorizationHeader){
		return ResponseEntity.ok(commentService.getAllCommentsOfExperience(experienceId, authorizationHeader));
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
