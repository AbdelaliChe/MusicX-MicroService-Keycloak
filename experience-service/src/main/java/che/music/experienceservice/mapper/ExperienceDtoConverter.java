package che.music.experienceservice.mapper;

import che.music.experienceservice.dto.ExperienceCreateDTO;
import che.music.experienceservice.dto.ExperienceReadDTO;
import che.music.experienceservice.dto.ExperienceUpdateDTO;
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

	private final ModelMapper modelMapper;

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
		modelMapper.typeMap(dto.getClass(), Experience.class)
				.addMappings(mapper -> {
					mapper.skip(Experience::setId);
				});

		return modelMapper.map(dto, Experience.class);
	}
}
