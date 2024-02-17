package che.music.experienceservice.dto;

import che.music.experienceservice.entity.Experience;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Component
public class ExperienceDtoConverter {

	private ModelMapper modelMapper;

	public ExperienceReadDTO convertToReadDTO(Experience experience) {
		return modelMapper.map(experience, ExperienceReadDTO.class);
	}

	public ExperienceUpdateDTO convertToUpdateDTO(Experience experience) {
		return modelMapper.map(experience, ExperienceUpdateDTO.class);
	}

	public ExperienceCreateDTO convertToCreateDTO(Experience experience) {
		return modelMapper.map(experience, ExperienceCreateDTO.class);
	}

	public Experience convertToEntity(Object dto) {
		return modelMapper.map(dto, Experience.class);
	}
}
