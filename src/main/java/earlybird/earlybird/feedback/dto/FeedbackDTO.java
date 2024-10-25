package earlybird.earlybird.feedback.dto;

import earlybird.earlybird.feedback.entity.Feedback;
import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserDetails;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import earlybird.earlybird.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedbackDTO {
    private Long id;
    private String content;
    private UserAccountInfoDTO userAccountInfoDTO;
    private LocalDateTime createdAt;

    @Builder
    private FeedbackDTO(Long id, String content, UserAccountInfoDTO userAccountInfoDTO, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.userAccountInfoDTO = userAccountInfoDTO;
        this.createdAt = createdAt;
    }

    public static FeedbackDTO of(OAuth2UserDetails oAuth2UserDetails, FeedbackRequestDTO requestDTO) {
        return FeedbackDTO.builder()
                .content(requestDTO.getContent())
                .userAccountInfoDTO(oAuth2UserDetails.getUserAccountInfoDTO())
                .createdAt(requestDTO.getCreatedAt())
                .build();
    }

    public static FeedbackDTO of(FeedbackRequestDTO requestDTO) {
        return FeedbackDTO.builder()
                .content(requestDTO.getContent())
                .createdAt(requestDTO.getCreatedAt())
                .build();
    }

    public static FeedbackDTO of(Feedback feedback) {
        return FeedbackDTO.builder()
                .id(feedback.getId())
                .content(feedback.getContent())
                .createdAt(feedback.getCreatedAt())
                .userAccountInfoDTO(feedback.getUser().toUserAccountInfoDTO())
                .build();
    }
}
