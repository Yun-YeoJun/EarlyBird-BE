package earlybird.earlybird.error.exception;

import static earlybird.earlybird.error.ErrorCode.ALREADY_SENT_FCM_NOTIFICATION;

public class AlreadySentFcmNotificationException extends BusinessBaseException {
    public AlreadySentFcmNotificationException() {
        super(ALREADY_SENT_FCM_NOTIFICATION);
    }
}
