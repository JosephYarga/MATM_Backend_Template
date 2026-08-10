package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.DomaineDTO;
import bf.gov.mtdpce.repository.DomaineRepository;
import bf.gov.mtdpce.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DomaineService {

    @Autowired
    private DomaineRepository domaineRepository;

    public Page<DomaineDTO> getAll(Pageable pageable) {
        return domaineRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public DomaineDTO getById(Long id) {
        Domaine domaine = domaineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé"));

        return convertToDTO(domaine);
    }

    public DomaineDTO create(DomaineDTO dto) {

        if (domaineRepository.existsByNom(dto.getNom())) {
            throw new RuntimeException("Un domaine avec ce nom existe déjà.");
        }

        Domaine domaine = new Domaine();
        domaine.setNom(dto.getNom());

        Domaine saved = domaineRepository.save(domaine);

        return convertToDTO(saved);
    }

    public DomaineDTO update(Long id, DomaineDTO dto) {

        Domaine domaine = domaineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé"));

        if (!domaine.getNom().equals(dto.getNom())
                && domaineRepository.existsByNom(dto.getNom())) {

            throw new RuntimeException("Un domaine avec ce nom existe déjà.");
        }

        domaine.setNom(dto.getNom());

        Domaine updated = domaineRepository.save(domaine);

        return convertToDTO(updated);
    }

    public void delete(Long id) {

        Domaine domaine = domaineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Domaine non trouvé"));

        domaineRepository.delete(domaine);
    }

    private DomaineDTO convertToDTO(Domaine domaine) {

        DomaineDTO dto = new DomaineDTO();
        dto.setId(domaine.getId());
        dto.setNom(domaine.getNom());

        return dto;
    }
}
