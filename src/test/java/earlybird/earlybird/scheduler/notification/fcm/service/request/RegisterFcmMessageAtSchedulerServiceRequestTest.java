package earlybird.earlybird.scheduler.notification.fcm.service.request;

import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

class RegisterFcmMessageAtSchedulerServiceRequestTest {

    @DisplayName("LocalDateTime 으로 저장되어 있는 전송 목표 시간을 Instant 로 변환해서 가져온다.")
    @Test
    void getTargetTimeInstant() {
        // given
        LocalDateTime targetTime = LocalDateTime.of(2024, 10, 11, 1, 2, 3);
        RegisterFcmMessageAtSchedulerServiceRequest request = RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .targetTime(targetTime)
                .build();

        // when
        Instant result = request.getTargetTimeInstant();

        // then
        assertThat(result.atZone(ZoneId.of("Asia/Seoul")).toString().split("\\+")[0])
                .isEqualTo(targetTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @DisplayName("RegisterFcmMessageAtSchedulerServiceRequest 객체를 FcmNotification 객체로 변환한다.")
    @Test
    void toFcmNotification() {
        // given
        LocalDateTime targetTime = LocalDateTime.of(2024, 10, 11, 1, 2, 3);
        RegisterFcmMessageAtSchedulerServiceRequest request = RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .title("제목")
                .body("바디")
                .deviceToken("디바이스 토큰")
                .targetTime(targetTime)
                .build();

        // when
        FcmNotification result = request.toFcmNotification();

        // then
        assertThat(result).extracting("uuid", "title", "body", "deviceToken", "targetTime", "status")
                .containsExactly(
                        request.getUuid(), request.getTitle(), request.getBody(), request.getDeviceToken(),
                        targetTime, PENDING
                );

    }

    @DisplayName("Builder 패턴을 이용해 객체를 생성할 수 있다.")
    @Test
    void builder() {
        // given
        String title = "title";
        String body = "body";
        String deviceToken = "deviceToken";
        LocalDateTime targetTime = LocalDateTime.of(2024, 10, 11, 1, 2, 3);

        // when
        RegisterFcmMessageAtSchedulerServiceRequest result = RegisterFcmMessageAtSchedulerServiceRequest.builder()
                .title(title)
                .body(body)
                .deviceToken(deviceToken)
                .targetTime(targetTime)
                .build();

        // then
        assertThat(result).extracting("title", "body", "deviceToken", "targetTime")
                .containsExactly(title, body, deviceToken, targetTime);
        assertThat(result.getUuid()).isNotNull();

    }



}