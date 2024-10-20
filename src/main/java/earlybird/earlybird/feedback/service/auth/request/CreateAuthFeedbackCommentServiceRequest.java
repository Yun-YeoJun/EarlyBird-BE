package earlybird.earlybird.feedback.service.auth.request;

import earlybird.earlybird.feedback.controller.request.CreateFeedbackCommentRequest;
import earlybird.earlybird.feedback.domain.comment.FeedbackComment;
import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserDetails;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateAuthFeedbackCommentServiceRequest {
    private Long id;
    private String comment;
    private UserAccountInfoDTO userAccountInfoDTO;
    private LocalDateTime createdAt;

    @Builder
    private CreateAuthFeedbackCommentServiceRequest(Long id, String comment, UserAccountInfoDTO userAccountInfoDTO, LocalDateTime createdAt) {
        this.id = id;
        this.comment = comment;
        this.userAccountInfoDTO = userAccountInfoDTO;
        this.createdAt = createdAt;
    }

    public static CreateAuthFeedbackCommentServiceRequest of(OAuth2UserDetails oAuth2UserDetails, CreateFeedbackCommentRequest requestDTO) {
        return CreateAuthFeedbackCommentServiceRequest.builder()
                .comment(requestDTO.getComment())
                .userAccountInfoDTO(oAuth2UserDetails.getUserAccountInfoDTO())
                .createdAt(requestDTO.getCreatedAt())
                .build();
    }
}
