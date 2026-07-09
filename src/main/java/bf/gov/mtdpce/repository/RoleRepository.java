package bf.gov.mtdpce.repository;
import java.util.UUID;

import bf.gov.mtdpce.entity.ERole;
import bf.gov.mtdpce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(ERole name);

    Boolean existsByName(ERole name);
}
