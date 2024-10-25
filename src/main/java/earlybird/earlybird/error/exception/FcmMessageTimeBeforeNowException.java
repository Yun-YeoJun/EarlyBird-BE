package earlybird.earlybird.error.exception;

import static earlybird.earlybird.error.ErrorCode.FCM_MESSAGE_TIME_BEFORE_NOW;

public class FcmMessageTimeBeforeNowException extends BusinessBaseException {

  public FcmMessageTimeBeforeNowException() {
    super(FCM_MESSAGE_TIME_BEFORE_NOW);
  }
}
