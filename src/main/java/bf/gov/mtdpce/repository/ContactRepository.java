package bf.gov.mtdpce.repository;

import bf.gov.mtdpce.entity.Contact;
import bf.gov.mtdpce.entity.ContactStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Page<Contact> findByStatus(ContactStatus status, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.subject) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Contact> searchContacts(@Param("search") String search, Pageable pageable);

    @Query("SELECT COUNT(c) FROM Contact c WHERE c.status = :status")
    Long countByStatus(@Param("status") ContactStatus status);

    @Query("SELECT COUNT(c) FROM Contact c WHERE c.status IN ('NON_LU', 'LU', 'EN_TRAITEMENT')")
    Long countPendingContacts();
}
