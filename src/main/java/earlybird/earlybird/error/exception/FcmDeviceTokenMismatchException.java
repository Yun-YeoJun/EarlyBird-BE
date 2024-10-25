package earlybird.earlybird.error.exception;

import static earlybird.earlybird.error.ErrorCode.FCM_DEVICE_TOKEN_MISMATCH;
import static earlybird.earlybird.error.ErrorCode.FCM_MESSAGE_TIME_BEFORE_NOW;

public class FcmDeviceTokenMismatchException extends BusinessBaseException {

    public FcmDeviceTokenMismatchException() {
      super(FCM_DEVICE_TOKEN_MISMATCH);
    }
}
