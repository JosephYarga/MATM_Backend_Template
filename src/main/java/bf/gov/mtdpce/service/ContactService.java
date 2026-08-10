package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.ContactDTO;
import bf.gov.mtdpce.entity.Contact;
import bf.gov.mtdpce.entity.ContactStatus;
import bf.gov.mtdpce.entity.User;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.ContactRepository;
import bf.gov.mtdpce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<ContactDTO> getAllContacts(Pageable pageable) {
        return contactRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<ContactDTO> getContactsByStatus(ContactStatus status, Pageable pageable) {
        return contactRepository.findByStatus(status, pageable).map(this::convertToDTO);
    }

    public Page<ContactDTO> searchContacts(String search, Pageable pageable) {
        return contactRepository.searchContacts(search, pageable).map(this::convertToDTO);
    }

    public ContactDTO getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        return convertToDTO(contact);
    }

    @Transactional
    public ContactDTO submitContact(ContactDTO contactDTO) {
        Contact contact = Contact.builder()
                .name(contactDTO.getName())
                .email(contactDTO.getEmail())
                .phone(contactDTO.getPhone())
                .subject(contactDTO.getSubject())
                .message(contactDTO.getMessage())
                .status(ContactStatus.NON_LU)
                .build();

        return convertToDTO(contactRepository.save(contact));
    }

    @Transactional
    public ContactDTO updateContactStatus(Long id, ContactStatus status) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        contact.setStatus(status);
        return convertToDTO(contactRepository.save(contact));
    }

    @Transactional
    public ContactDTO respondToContact(Long id, String response, Long respondedById) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));

        User respondedBy = userRepository.findById(respondedById)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", respondedById));

        contact.setResponse(response);
        contact.setRespondedBy(respondedBy);
        contact.setRespondedAt(LocalDateTime.now());
        contact.setStatus(ContactStatus.TRAITE);

        return convertToDTO(contactRepository.save(contact));
    }

    @Transactional
    public void deleteContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact", "id", id);
        }
        contactRepository.deleteById(id);
    }

    public Long countPendingContacts() {
        return contactRepository.countPendingContacts();
    }

    private ContactDTO convertToDTO(Contact contact) {
        ContactDTO.ContactDTOBuilder builder = ContactDTO.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .phone(contact.getPhone())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .status(contact.getStatus())
                .response(contact.getResponse())
                .respondedAt(contact.getRespondedAt())
                .createdAt(contact.getCreatedAt());

        if (contact.getRespondedBy() != null) {
            builder.respondedByName(contact.getRespondedBy().getFirstName() + " " + contact.getRespondedBy().getLastName())
                    .respondedById(contact.getRespondedBy().getId());
        }

        return builder.build();
    }
}
