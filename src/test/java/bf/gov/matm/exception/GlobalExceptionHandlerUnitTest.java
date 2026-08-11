package bf.gov.matm.exception;

import bf.gov.matm.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test UNITAIRE pur du GlobalExceptionHandler :
 * chaque exception doit être traduite vers le bon code HTTP (plus de 500 fourre-tout).
 */
class GlobalExceptionHandlerUnitTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void resourceNotFound_maps_to_404() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleResourceNotFoundException(new ResourceNotFoundException("Domaine non trouvé"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void badRequest_maps_to_400() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleBadRequestException(new BadRequestException("invalide"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void missingParam_maps_to_400() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleMissingParam(new MissingServletRequestParameterException("query", "String"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void notReadable_maps_to_400() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleNotReadable(new HttpMessageNotReadableException("boom"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void illegalArgument_maps_to_400() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleIllegalArgument(new IllegalArgumentException("Name is null"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void invalidDataAccess_maps_to_400() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleInvalidDataAccess(new InvalidDataAccessApiUsageException("The given id must not be null"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void mediaTypeNotSupported_maps_to_415() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleMediaType(new HttpMediaTypeNotSupportedException("application/json non supporté"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    void dataIntegrity_maps_to_409() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleDataIntegrity(new DataIntegrityViolationException("doublon"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void unhandled_maps_to_500() {
        ResponseEntity<ApiResponse<Void>> r =
                handler.handleGlobalException(new Exception("inattendu"));
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
