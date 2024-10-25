package earlybird.earlybird.feedback.service;

import earlybird.earlybird.error.exception.UserNotFoundException;
import earlybird.earlybird.feedback.dto.FeedbackDTO;
import earlybird.earlybird.feedback.repository.FeedbackRepository;
import earlybird.earlybird.user.dto.UserAccountInfoDTO;
import earlybird.earlybird.user.entity.User;
import earlybird.earlybird.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class CreateAuthUserFeedbackServiceTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateAuthUserFeedbackService createAuthUserFeedbackService;


    @AfterEach
    void tearDown() {
        feedbackRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("로그인한 유저의 피드백을 생성한다.")
    @Test
    void create() {
        // given
        LocalDateTime createdAt = LocalDateTime.of(2024, 10, 7, 9, 0);
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .accountId("id")
                .email("email@email.com")
                .name("name")
                .role("USER")
                .createdAt(createdAt)
                .build();

        User savedUser = userRepository.save(user);

        UserAccountInfoDTO userAccountInfoDTO = savedUser.toUserAccountInfoDTO();

        Long feedbackId = 1L;
        String feedbackContent = "feedback content";
        FeedbackDTO feedbackDTO = FeedbackDTO.builder()
                .id(feedbackId)
                .content(feedbackContent)
                .userAccountInfoDTO(userAccountInfoDTO)
                .createdAt(createdAt)
                .build();

        // when
        FeedbackDTO createdFeedbackDTO = createAuthUserFeedbackService.create(feedbackDTO);

        // then
        assertThat(feedbackRepository.findAll()).hasSize(1);
        assertThat(createdFeedbackDTO)
                .extracting("id", "content", "createdAt")
                .contains(
                        feedbackId, feedbackContent, createdAt
                );
        assertThat(createdFeedbackDTO.getUserAccountInfoDTO()).isEqualTo(userAccountInfoDTO);
    }


    @DisplayName("찾을 수 없는 유저가 피드백 생성을 요청하면 예외가 발생한다.")
    @Test
    void createWithNotFoundUser() {
        // given
        UserAccountInfoDTO userAccountInfoDTO = UserAccountInfoDTO.builder()
                .accountId("id")
                .id(1L)
                .email("email@email.com")
                .name("name")
                .role("USER")
                .build();

        FeedbackDTO feedbackDTO = FeedbackDTO.builder()
                .userAccountInfoDTO(userAccountInfoDTO)
                .build();

        // when // then
        assertThatThrownBy(() -> createAuthUserFeedbackService.create(feedbackDTO))
                .isInstanceOf(UserNotFoundException.class);
    }

}