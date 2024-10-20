package earlybird.earlybird.feedback.service.auth;

import earlybird.earlybird.error.exception.UserNotFoundException;
import earlybird.earlybird.feedback.domain.comment.FeedbackComment;
import earlybird.earlybird.feedback.domain.comment.FeedbackCommentRepository;
import earlybird.earlybird.feedback.service.auth.request.CreateAuthFeedbackCommentServiceRequest;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateAuthFeedbackCommentService {

    private final FeedbackCommentRepository feedbackCommentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(CreateAuthFeedbackCommentServiceRequest request) {

        Long userId = request.getUserAccountInfoDTO().getId();

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        FeedbackComment feedbackComment = FeedbackComment.builder()
                .comment(request.getComment())
                .user(user)
                .createdTimeAtClient(request.getCreatedAt())
                .build();

        feedbackCommentRepository.save(feedbackComment);
    }
}
