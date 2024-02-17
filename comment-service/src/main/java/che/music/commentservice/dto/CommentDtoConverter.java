package che.music.commentservice.dto;

import che.music.commentservice.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Component
public class CommentDtoConverter {

	private ModelMapper modelMapper;

	public CommentReadDTO convertToReadDTO(Comment comment) {
		return modelMapper.map(comment, CommentReadDTO.class);
	}

	public CommentUpdateDTO convertToUpdateDTO(Comment comment) {
		return modelMapper.map(comment, CommentUpdateDTO.class);
	}

	public CommentCreateDTO convertToCreateDTO(Comment comment) {
		return modelMapper.map(comment, CommentCreateDTO.class);
	}

	public Comment convertToEntity(Object dto) {
		return modelMapper.map(dto, Comment.class);
	}
}
