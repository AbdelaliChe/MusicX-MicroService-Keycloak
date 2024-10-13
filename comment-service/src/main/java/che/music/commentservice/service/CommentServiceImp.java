package che.music.commentservice.service;

import che.music.commentservice.dto.*;
import che.music.commentservice.entity.Comment;
import che.music.commentservice.exception.NotFoundException;
import che.music.commentservice.exception.NotYoursException;
import che.music.commentservice.feign.Auth0Client;
import che.music.commentservice.feign.ExperienceClient;
import che.music.commentservice.mapper.CommentDtoConverter;
import che.music.commentservice.repository.ICommentRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImp implements ICommentService {

	private final ICommentRepo commentRepo;
	private final Auth0Client auth0Client;
	private final CommentDtoConverter commentDtoConverter;
	private final ExperienceClient experienceClient;

	private final OAuth2AuthorizedClientService authorizedClientService;
	private final ClientRegistrationRepository clientRegistrationRepository;

	@Value("${okta.oauth2.clientId}")
	private String clientId;

	@Value("${okta.oauth2.clientSecret}")
	private String clientSecret;

	@Value("${okta.oauth2.audience}")
	private String audience;

	@Transactional
	@Override
	public CommentReadDTO createComment(CommentCreateDTO commentCreateDTO) {
		// TODO : check for experience existence!
		/*if(!isExperienceExist(commentCreateDTO.getExperienceId()))
			throw new NotFoundException("Experience with Id : "+ commentCreateDTO.getExperienceId() + "does not exist!");
		*/
		Comment comment = commentDtoConverter.convertToEntity(commentCreateDTO);
		String userId = (String) getCurrentUser().get("sub");
		comment.setUserId(userId);
		CommentReadDTO experienceReadDTO = commentDtoConverter.convertToReadDTO(
				commentRepo.save(comment));

		return experienceReadDTO;
	}

	@Transactional
	@Override
	public CommentReadDTO updateComment(Long id, CommentUpdateDTO commentUpdateDTO) {
		Comment commentToEdit = getCommentById(id);
		String userId = (String) getCurrentUser().get("sub");
		if (!commentToEdit.getUserId().equals(userId)) {
			throw new NotFoundException("You can't update comment with id: " + id + " because you don't own it.");
		}
		commentToEdit.setContent(commentUpdateDTO.getContent());
		commentToEdit.setUpdatedAt( LocalDateTime.now());
		return commentDtoConverter.convertToReadDTO(commentRepo.save(commentToEdit));
	}

	@Transactional
	@Override
	public boolean deleteComment(Long id) {
		Comment comment = getCommentById(id);
		String userId = (String) getCurrentUser().get("sub");
		if(comment.getUserId().equals(userId)){
			commentRepo.delete(comment);
			return true;
		}else{
			throw new NotYoursException("You can't delete comment with id: " + id + " because you don't own it.");
		}
	}

	@Override
	public List<CommentReadDTO> getCommentsByExperienceId(Long experienceId) {
		List<Comment> comments = commentRepo.findCommentsByExperienceId(experienceId);
		return comments.stream().map(comment -> {
			CommentReadDTO commentReadDTO = commentDtoConverter.convertToReadDTO(comment);
			commentReadDTO.setUser(this.buildUser(comment.getUserId()));
			return commentReadDTO;
		}).collect(Collectors.toList());
	}

	private User buildUser(String userId) {
		String token = this.getToken();
		String authorizationHeader = "Bearer " + token;
		ResponseEntity<User> response = auth0Client.getUserById(userId, authorizationHeader);
		if(response.getStatusCode().equals(HttpStatus.OK)) return response.getBody();
		throw new NotFoundException("User with id : "+ userId + "doesn't exit!");
	}

	private Comment getCommentById(Long id) {
		return commentRepo.findById(id).orElseThrow(() ->
				new NotFoundException("Comment not found with id: " + id));
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

	private boolean isExperienceExist(Long experienceId) {
		String authorizationHeader = "Bearer " + this.getCurrentAccessToken();
		log.info("token : {}",authorizationHeader);

		ResponseEntity<?> response = experienceClient
				.getExperienceById(experienceId, authorizationHeader);
		return response.getStatusCode().equals(HttpStatus.OK);
	}

	private String getCurrentAccessToken() {
		//
		return null;
	}
}
