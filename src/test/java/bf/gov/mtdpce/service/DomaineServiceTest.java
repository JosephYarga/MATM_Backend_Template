package bf.gov.mtdpce.service;

import bf.gov.mtdpce.dto.request.DomaineRequest;
import bf.gov.mtdpce.dto.response.DomaineResponse;
import bf.gov.mtdpce.entity.Domaine;
import bf.gov.mtdpce.exception.BadRequestException;
import bf.gov.mtdpce.exception.ResourceNotFoundException;
import bf.gov.mtdpce.repository.DomaineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test UNITAIRE pur (sans contexte Spring, sans base de données) :
 * la logique de DomaineService est testée en isolant le repository avec Mockito.
 */
@ExtendWith(MockitoExtension.class)
class DomaineServiceTest {

    @Mock
    private DomaineRepository domaineRepository;

    @InjectMocks
    private DomaineService domaineService;

    @Test
    void getById_whenNotFound_throwsResourceNotFound() {
        UUID id = UUID.randomUUID();
        when(domaineRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> domaineService.getById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("non trouvé");
    }

    @Test
    void create_whenNameAlreadyExists_throwsBadRequest() {
        DomaineRequest dto = new DomaineRequest();
        dto.setNom("Cybersécurité");
        when(domaineRepository.existsByNom("Cybersécurité")).thenReturn(true);

        assertThatThrownBy(() -> domaineService.create(dto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("existe déjà");

        verify(domaineRepository, never()).save(any());
    }

    @Test
    void create_whenNameIsNew_savesAndReturnsDto() {
        DomaineRequest dto = new DomaineRequest();
        dto.setNom("Innovation");
        when(domaineRepository.existsByNom("Innovation")).thenReturn(false);

        Domaine saved = new Domaine();
        saved.setNom("Innovation");
        when(domaineRepository.save(any(Domaine.class))).thenReturn(saved);

        DomaineResponse result = domaineService.create(dto);

        assertThat(result.getNom()).isEqualTo("Innovation");
        verify(domaineRepository).save(any(Domaine.class));
    }
}
