package ai.conexa.challenge.unit.exception;

import ai.conexa.challenge.exception.InvalidCredentialsException;
import ai.conexa.challenge.exception.handler.ErrorResponse;
import ai.conexa.challenge.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

import static ai.conexa.challenge.util.MessageConstants.INVALID_CREDENTIALS;
import static ai.conexa.challenge.util.MessageConstants.SWAPI_FETCHING_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleResourceAccessException() {
        ResourceAccessException ex = mock(ResourceAccessException.class);
        when(ex.getMessage()).thenReturn(SWAPI_FETCHING_ERROR);
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceAccessException(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals(SWAPI_FETCHING_ERROR, response.getBody().getMessage());
    }

    @Test
    void testHandleInvalidCredentialsException() {
        InvalidCredentialsException ex = mock(InvalidCredentialsException.class);
        when(ex.getMessage()).thenReturn(INVALID_CREDENTIALS);
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidCredentialsException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(INVALID_CREDENTIALS, response.getBody().getMessage());
    }

    @Test
    void testHandleRestClientException() {
        RestClientException ex = mock(RestClientException.class);
        when(ex.getMessage()).thenReturn("Rest client exception");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleRestClientException(ex);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertEquals("Rest client exception", response.getBody().getMessage());
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getValue()).thenReturn("invalidValue");
        when(ex.getName()).thenReturn("parameterName");
        when(ex.getRequiredType()).thenReturn((Class) String.class);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleMethodArgumentTypeMismatchException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("'invalidValue' is not a valid value for the field 'parameterName'. Expected type: String.",
                response.getBody().getMessage());
    }

    @Test
    void testHandleConstraintViolationException() {
        ConstraintViolationException ex = mock(ConstraintViolationException.class);
        when(ex.getMessage()).thenReturn("Constraint violation occurred");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation error", response.getBody().getMessage());
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Internal server error");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().getMessage());
    }
}
