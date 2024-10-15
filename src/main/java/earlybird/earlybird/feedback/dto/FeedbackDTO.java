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
    private Long score;
    private UserAccountInfoDTO userAccountInfoDTO;
    private LocalDateTime createdAt;

    @Builder
    private FeedbackDTO(Long id, String content, Long score, UserAccountInfoDTO userAccountInfoDTO, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.score = score;
        this.userAccountInfoDTO = userAccountInfoDTO;
        this.createdAt = createdAt;
    }

    public static FeedbackDTO of(OAuth2UserDetails oAuth2UserDetails, FeedbackRequestDTO requestDTO) {
        return FeedbackDTO.builder()
                .content(requestDTO.getContent())
                .score(requestDTO.getScore())
                .userAccountInfoDTO(oAuth2UserDetails.getUserAccountInfoDTO())
                .createdAt(requestDTO.getCreatedAt())
                .build();
    }

    public static FeedbackDTO of(FeedbackRequestDTO requestDTO) {
        return FeedbackDTO.builder()
                .content(requestDTO.getContent())
                .score(requestDTO.getScore())
                .createdAt(requestDTO.getCreatedAt())
                .build();
    }

    public static FeedbackDTO of(Feedback feedback) {
        UserAccountInfoDTO userAccountInfoDTO = null;
        if (feedback.getUser() != null) {
            userAccountInfoDTO = feedback.getUser().toUserAccountInfoDTO();
        }

        return FeedbackDTO.builder()
                .id(feedback.getId())
                .content(feedback.getContent())
                .score(feedback.getScore())
                .createdAt(feedback.getCreatedAt())
                .userAccountInfoDTO(userAccountInfoDTO)
                .build();
    }
}
