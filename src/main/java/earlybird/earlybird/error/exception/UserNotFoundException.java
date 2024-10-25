package earlybird.earlybird.error.exception;

import earlybird.earlybird.error.ErrorCode;

public class UserNotFoundException extends NotFoundException {
  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }
}
