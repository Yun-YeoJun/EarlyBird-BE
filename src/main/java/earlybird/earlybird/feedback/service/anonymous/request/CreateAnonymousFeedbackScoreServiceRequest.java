package earlybird.earlybird.feedback.service.anonymous.request;

import earlybird.earlybird.feedback.controller.request.CreateFeedbackScoreRequest;
import earlybird.earlybird.feedback.domain.score.FeedbackScore;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateAnonymousFeedbackScoreServiceRequest {
    private int score;
    private String clientId;
    private LocalDateTime createdAt;

    @Builder
    private CreateAnonymousFeedbackScoreServiceRequest(int score, String clientId, LocalDateTime createdAt) {
        this.score = score;
        this.clientId = clientId;
        this.createdAt = createdAt;
    }

    public static CreateAnonymousFeedbackScoreServiceRequest of(CreateFeedbackScoreRequest request) {
        return CreateAnonymousFeedbackScoreServiceRequest.builder()
                .score(request.getScore())
                .clientId(request.getClientId())
                .createdAt(request.getCreatedAt())
                .build();
    }
}
