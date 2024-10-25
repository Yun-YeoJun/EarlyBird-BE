package earlybird.earlybird.feedback.controller;

import earlybird.earlybird.feedback.service.CreateAnonymousUserFeedbackService;
import earlybird.earlybird.feedback.service.CreateAuthUserFeedbackService;
import earlybird.earlybird.feedback.dto.FeedbackDTO;
import earlybird.earlybird.feedback.dto.FeedbackRequestDTO;
import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserDetails;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RequestMapping("/api/v1/feedbacks")
@RestController
public class CreateFeedbackController {

    private final CreateAuthUserFeedbackService createAuthUserFeedbackService;
    private final CreateAnonymousUserFeedbackService createAnonymousUserFeedbackService;

    @PostMapping
    public ResponseEntity<?> createFeedback(
            @AuthenticationPrincipal OAuth2UserDetails oAuth2UserDetails,
            @RequestBody FeedbackRequestDTO requestDTO) {

        if (oAuth2UserDetails != null) {
            createAuthUserFeedback(oAuth2UserDetails, requestDTO);
        }
        else {
            createAnonymousUserFeedback(requestDTO);
        }

        return ResponseEntity.ok().build();
    }

    private void createAuthUserFeedback(OAuth2UserDetails oAuth2UserDetails, FeedbackRequestDTO requestDTO) {
        FeedbackDTO feedbackDTO = FeedbackDTO.of(oAuth2UserDetails, requestDTO);
        createAuthUserFeedbackService.create(feedbackDTO);
    }

    private void createAnonymousUserFeedback(FeedbackRequestDTO requestDTO) {
        FeedbackDTO feedbackDTO = FeedbackDTO.of(requestDTO);
        createAnonymousUserFeedbackService.create(feedbackDTO);
    }

}
