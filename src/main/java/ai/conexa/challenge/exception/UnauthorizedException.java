package ai.conexa.challenge.exception;

import static ai.conexa.challenge.util.MessageConstants.UNAUTHORIZED;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super(UNAUTHORIZED);
    }
}
