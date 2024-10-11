package earlybird.earlybird.error.exception;

import earlybird.earlybird.error.ErrorCode;

public class FcmNotificationNotFoundException extends NotFoundException {
    public FcmNotificationNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}
