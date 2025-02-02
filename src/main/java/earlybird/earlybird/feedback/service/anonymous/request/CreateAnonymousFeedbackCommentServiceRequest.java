package earlybird.earlybird.feedback.service.anonymous.request;

import earlybird.earlybird.feedback.controller.request.CreateFeedbackCommentRequest;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateAnonymousFeedbackCommentServiceRequest {
    private String comment;
    private String clientId;
    private LocalDateTime createdAt;

    @Builder
    private CreateAnonymousFeedbackCommentServiceRequest(
            String comment, String clientId, LocalDateTime createdAt) {
        this.comment = comment;
        this.clientId = clientId;
        this.createdAt = createdAt;
    }

    public static CreateAnonymousFeedbackCommentServiceRequest of(
            CreateFeedbackCommentRequest request) {
        return CreateAnonymousFeedbackCommentServiceRequest.builder()
                .comment(request.getComment())
                .clientId(request.getClientId())
                .createdAt(request.getCreatedAt())
                .build();
    }
}
