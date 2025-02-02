package earlybird.earlybird.feedback.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateFeedbackCommentRequest {
    @NotBlank private String comment;
    @NotBlank private String clientId;

    @NotNull
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
