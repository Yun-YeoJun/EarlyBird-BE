package earlybird.earlybird.error.exception;

import earlybird.earlybird.error.ErrorCode;

public class NotificationNotFoundException extends NotFoundException {
    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
}
