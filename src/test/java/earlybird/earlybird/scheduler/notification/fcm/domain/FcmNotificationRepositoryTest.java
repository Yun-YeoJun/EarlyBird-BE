package earlybird.earlybird.scheduler.notification.fcm.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FcmNotificationRepositoryTest {

    @Autowired
    private FcmNotificationRepository fcmNotificationRepository;

    @AfterEach
    void tearDown() {
        fcmNotificationRepository.deleteAllInBatch();
    }

    @DisplayName("UUID 로 저장된 FCM 메시지를 조회한다.")
    @Test
    void findByUuid() {
        // given
        String uuid = "uuid";
        FcmNotification notification = createNotification(uuid);
        fcmNotificationRepository.save(notification);

        // when
        Optional<FcmNotification> optionalFcmNotification = fcmNotificationRepository.findByUuid(uuid);

        // then
        assertThat(optionalFcmNotification).isPresent();
        assertThat(optionalFcmNotification.get().getUuid()).isEqualTo(uuid);
    }

    private FcmNotification createNotification(String uuid) {
        return FcmNotification.builder()
                .uuid(uuid)
                .deviceToken("디바이스 토큰")
                .title("메시지 제목")
                .targetTime(LocalDateTime.of(2024, 10, 11, 0, 0))
                .body("메시지 바디")
                .build();
    }

}