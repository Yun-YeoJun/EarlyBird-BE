package earlybird.earlybird.feedback.service;

import earlybird.earlybird.error.exception.UserNotFoundException;
import earlybird.earlybird.feedback.entity.Feedback;
import earlybird.earlybird.feedback.repository.FeedbackRepository;
import earlybird.earlybird.feedback.dto.FeedbackDTO;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreateAuthUserFeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public FeedbackDTO create(FeedbackDTO feedbackDTO) {

        Long userId = feedbackDTO.getUserAccountInfoDTO().getId();

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Feedback feedback = Feedback.builder()
                .content(feedbackDTO.getContent())
                .user(user)
                .createdAt(feedbackDTO.getCreatedAt())
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return FeedbackDTO.of(savedFeedback);
    }
}
