package ai.conexa.challenge.exception;

import static ai.conexa.challenge.util.MessageConstants.INVALID_CREDENTIALS;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super(INVALID_CREDENTIALS);
    }
}
