package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Project;
import bf.gov.mtdpce.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByStatus(ProjectStatus status, Pageable pageable);

    List<Project> findTop5ByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Project p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Project> searchProjects(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    Long countByStatus(@Param("status") ProjectStatus status);

    @Query("SELECT AVG(p.progressPercentage) FROM Project p WHERE p.status = 'EN_COURS'")
    Double getAverageProgress();
}
