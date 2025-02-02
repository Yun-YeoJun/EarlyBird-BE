package earlybird.earlybird.feedback.controller;

import earlybird.earlybird.feedback.controller.request.CreateFeedbackCommentRequest;
import earlybird.earlybird.feedback.controller.request.CreateFeedbackScoreRequest;
import earlybird.earlybird.feedback.service.anonymous.CreateAnonymousFeedbackCommentService;
import earlybird.earlybird.feedback.service.anonymous.CreateAnonymousFeedbackScoreService;
import earlybird.earlybird.feedback.service.anonymous.request.CreateAnonymousFeedbackCommentServiceRequest;
import earlybird.earlybird.feedback.service.anonymous.request.CreateAnonymousFeedbackScoreServiceRequest;
import earlybird.earlybird.feedback.service.auth.CreateAuthFeedbackCommentService;
import earlybird.earlybird.feedback.service.auth.request.CreateAuthFeedbackCommentServiceRequest;
import earlybird.earlybird.security.authentication.oauth2.user.OAuth2UserDetails;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/feedbacks")
@RestController
public class CreateFeedbackController {

    private final CreateAuthFeedbackCommentService createAuthFeedbackCommentService;
    private final CreateAnonymousFeedbackCommentService createAnonymousFeedbackCommentService;
    private final CreateAnonymousFeedbackScoreService createAnonymousFeedbackScoreService;

    @PostMapping("/comments")
    public ResponseEntity<?> createFeedbackComment(
            @AuthenticationPrincipal OAuth2UserDetails oAuth2UserDetails,
            @Valid @RequestBody CreateFeedbackCommentRequest request) {

        if (oAuth2UserDetails != null) {
            CreateAuthFeedbackCommentServiceRequest serviceRequest =
                    CreateAuthFeedbackCommentServiceRequest.of(oAuth2UserDetails, request);
            createAuthFeedbackCommentService.create(serviceRequest);
        } else {
            CreateAnonymousFeedbackCommentServiceRequest serviceRequest =
                    CreateAnonymousFeedbackCommentServiceRequest.of(request);
            createAnonymousFeedbackCommentService.create(serviceRequest);
        }

        return ResponseEntity.ok().build();
    }

    // TODO: 베타 테스트 이후 로그인 사용자의 피드백 받는 기능 구현
    @PostMapping("/scores")
    public ResponseEntity<?> createFeedbackScore(
            @Valid @RequestBody CreateFeedbackScoreRequest request) {

        CreateAnonymousFeedbackScoreServiceRequest serviceRequest =
                CreateAnonymousFeedbackScoreServiceRequest.of(request);
        createAnonymousFeedbackScoreService.create(serviceRequest);

        return ResponseEntity.ok().build();
    }
}
