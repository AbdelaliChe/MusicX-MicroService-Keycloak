package che.music.experienceservice.repository;

import che.music.experienceservice.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface IExperienceRepo extends JpaRepository<Experience,Long> {
	@Query("SELECT e FROM Experience e WHERE e.archived = false")
	Page<Experience> findAllNonArchived(Pageable pageable);

	List<Experience> findByUserId(String userId);
	List<Experience> findExperienceBySpotifyItemId(String itemId);
}
