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
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }
}
