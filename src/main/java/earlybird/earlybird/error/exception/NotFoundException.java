package earlybird.earlybird.error.exception;

import earlybird.earlybird.error.ErrorCode;

public class NotFoundException extends BusinessBaseException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage(), errorCode);
    }

    public NotFoundException() {
        super((ErrorCode.NOT_FOUND));
    }
}
