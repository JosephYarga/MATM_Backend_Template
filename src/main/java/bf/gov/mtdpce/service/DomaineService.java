package bf.gov.mtdpce.service;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.exception.BadRequestException;
import java.util.UUID;

import bf.gov.mtdpce.dto.request.DomaineRequest;
import bf.gov.mtdpce.dto.response.DomaineResponse;
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

    public Page<DomaineResponse> getAll(Pageable pageable) {
        return domaineRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    public DomaineResponse getById(UUID id) {
        Domaine domaine = domaineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Domaine non trouvé"));

        return convertToResponse(domaine);
    }

    public DomaineResponse create(DomaineRequest dto) {

        if (domaineRepository.existsByNom(dto.getNom())) {
            throw new BadRequestException("Un domaine avec ce nom existe déjà.");
        }

        Domaine domaine = new Domaine();
        domaine.setNom(dto.getNom());

        Domaine saved = domaineRepository.save(domaine);

        return convertToResponse(saved);
    }

    public DomaineResponse update(UUID id, DomaineRequest dto) {

        Domaine domaine = domaineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Domaine non trouvé"));

        if (!domaine.getNom().equals(dto.getNom())
                && domaineRepository.existsByNom(dto.getNom())) {

            throw new BadRequestException("Un domaine avec ce nom existe déjà.");
        }

        domaine.setNom(dto.getNom());

        Domaine updated = domaineRepository.save(domaine);

        return convertToResponse(updated);
    }

    public void delete(UUID id) {

        Domaine domaine = domaineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Domaine non trouvé"));

        domaineRepository.delete(domaine);
    }

    private DomaineResponse convertToResponse(Domaine domaine) {

        DomaineResponse response = new DomaineResponse();
        response.setId(domaine.getId());
        response.setNom(domaine.getNom());

        return response;
    }
}
