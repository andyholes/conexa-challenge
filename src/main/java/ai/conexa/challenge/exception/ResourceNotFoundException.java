package ai.conexa.challenge.exception;

import static ai.conexa.challenge.util.MessageConstants.RESOURCE_NOT_FOUND;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super(RESOURCE_NOT_FOUND);
    }
}
