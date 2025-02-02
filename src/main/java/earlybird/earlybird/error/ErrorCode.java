package earlybird.earlybird.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "올바르지 않은 입력값입니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP 메서드를 호출했습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 엔티티입니다."),
  ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 아티클입니다."),
  NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),
  FCM_MESSAGE_TIME_BEFORE_NOW(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 메시지 전송 희망 시간이 현재보다 과거입니다."),
  FCM_NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다."),
  ALREADY_SENT_FCM_NOTIFICATION(HttpStatus.BAD_REQUEST, "이미 전송된 FCM 알림입니다."),
  INCORRECT_REQUEST_BODY_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 request body 형식입니다."),
  INVALID_REQUEST_ARGUMENT(HttpStatus.BAD_REQUEST, "request argument 가 제약 조건을 만족하지 않습니다."),
  FCM_DEVICE_TOKEN_MISMATCH(
      HttpStatus.BAD_REQUEST, "요청한 알림 ID에 해당하는 디바이스 토큰과 요청한 디바이스 토큰이 일치하지 않습니다."),
  APPOINTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 약속입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
  DELETED_APPOINTMENT_EXCEPTION(HttpStatus.NOT_FOUND, "삭제된 일정입니다.");

  private final HttpStatus status;
  private final String message;

  ErrorCode(final HttpStatus status, final String message) {
    this.status = status;
    this.message = message;
  }
}
