package che.music.experienceservice.repository;

import che.music.experienceservice.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExperienceDao extends JpaRepository<Experience,Long> {
	Experience findByName(String name);

	List<Experience> findByUserId(String userId);
}
