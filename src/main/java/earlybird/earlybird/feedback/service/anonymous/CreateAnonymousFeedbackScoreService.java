package earlybird.earlybird.feedback.service.anonymous;

import earlybird.earlybird.feedback.domain.score.FeedbackScore;
import earlybird.earlybird.feedback.domain.score.FeedbackScoreRepository;
import earlybird.earlybird.feedback.service.anonymous.request.CreateAnonymousFeedbackScoreServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateAnonymousFeedbackScoreService {

  private final FeedbackScoreRepository feedbackScoreRepository;

  @Transactional
  public void create(CreateAnonymousFeedbackScoreServiceRequest request) {
    FeedbackScore feedbackScore =
        FeedbackScore.builder()
            .score(request.getScore())
            .clientId(request.getClientId())
            .createdTimeAtClient(request.getCreatedAt())
            .build();

    feedbackScoreRepository.save(feedbackScore);
  }
}
