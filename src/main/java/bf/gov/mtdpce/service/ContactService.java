package bf.gov.mtdpce.service;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.ContactRequest;
import bf.gov.mtdpce.dto.response.ContactResponse;
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

    public Page<ContactResponse> getAllContacts(Pageable pageable) {
        return contactRepository.findAll(pageable).map(this::convertToResponse);
    }

    public Page<ContactResponse> getContactsByStatus(ContactStatus status, Pageable pageable) {
        return contactRepository.findByStatus(status, pageable).map(this::convertToResponse);
    }

    public Page<ContactResponse> searchContacts(String search, Pageable pageable) {
        return contactRepository.searchContacts(search, pageable).map(this::convertToResponse);
    }

    public ContactResponse getContactById(UUID id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        return convertToResponse(contact);
    }

    @Transactional
    public ContactResponse submitContact(ContactRequest contactDTO) {
        Contact contact = Contact.builder()
                .name(contactDTO.getName())
                .email(contactDTO.getEmail())
                .phone(contactDTO.getPhone())
                .subject(contactDTO.getSubject())
                .message(contactDTO.getMessage())
                .status(ContactStatus.NON_LU)
                .build();

        return convertToResponse(contactRepository.save(contact));
    }

    @Transactional
    public ContactResponse updateContactStatus(UUID id, ContactStatus status) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));
        contact.setStatus(status);
        return convertToResponse(contactRepository.save(contact));
    }

    @Transactional
    public ContactResponse respondToContact(UUID id, String response, UUID respondedById) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact", "id", id));

        User respondedBy = userRepository.findById(respondedById)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", respondedById));

        contact.setResponse(response);
        contact.setRespondedBy(respondedBy);
        contact.setRespondedAt(LocalDateTime.now());
        contact.setStatus(ContactStatus.TRAITE);

        return convertToResponse(contactRepository.save(contact));
    }

    @Transactional
    public void deleteContact(UUID id) {
        if (!contactRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact", "id", id);
        }
        contactRepository.deleteById(id);
    }

    public Long countPendingContacts() {
        return contactRepository.countPendingContacts();
    }

    private ContactResponse convertToResponse(Contact contact) {
        ContactResponse.ContactResponseBuilder builder = ContactResponse.builder()
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
