package earlybird.earlybird.feedback.service.anonymous;

import earlybird.earlybird.feedback.domain.comment.FeedbackComment;
import earlybird.earlybird.feedback.domain.comment.FeedbackCommentRepository;
import earlybird.earlybird.feedback.service.anonymous.request.CreateAnonymousFeedbackCommentServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateAnonymousFeedbackCommentService {

  private final FeedbackCommentRepository feedbackCommentRepository;

  @Transactional
  public void create(CreateAnonymousFeedbackCommentServiceRequest request) {
    FeedbackComment feedbackComment =
        FeedbackComment.builder()
            .comment(request.getComment())
            .clientId(request.getClientId())
            .createdTimeAtClient(request.getCreatedAt())
            .build();

    feedbackCommentRepository.save(feedbackComment);
  }
}
