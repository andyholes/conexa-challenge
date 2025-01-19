package ai.conexa.challenge.exception;

import static ai.conexa.challenge.util.MessageConstants.JSON_PARSE_ERROR;

public class JsonParsingException extends RuntimeException {
    public JsonParsingException() {
        super(JSON_PARSE_ERROR);
    }
}
