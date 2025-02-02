package earlybird.earlybird.error.exception;

import earlybird.earlybird.error.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BusinessBaseException extends RuntimeException {
  private final ErrorCode errorCode;

  public BusinessBaseException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public BusinessBaseException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
