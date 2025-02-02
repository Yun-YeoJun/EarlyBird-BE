package earlybird.earlybird.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
  private HttpStatus status;
  private String message;

  private ErrorResponse(final ErrorCode errorCode) {
    this.status = errorCode.getStatus();
    this.message = errorCode.getMessage();
  }

  public ErrorResponse(final ErrorCode errorCode, final String message) {
    this.status = errorCode.getStatus();
    this.message = errorCode.getMessage();
  }

  public static ErrorResponse of(final ErrorCode errorCode) {
    return new ErrorResponse(errorCode);
  }

  public static ErrorResponse of(final ErrorCode errorCode, final String message) {
    return new ErrorResponse(errorCode, message);
  }
}
