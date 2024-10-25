package earlybird.earlybird.feedback.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class FeedbackRequestDTO {
    private String content;

    /**
     * format: "yyyy-MM-dd HH:mm:ss.SSS"
     */
    private String createdAt;

    public LocalDateTime getCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(this.createdAt, formatter);
    }
}
