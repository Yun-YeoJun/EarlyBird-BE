package earlybird.earlybird.scheduler.notification.service.update;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.CreateTestAppointment;
import earlybird.earlybird.appointment.service.FindAppointmentService;
import earlybird.earlybird.scheduler.notification.domain.NotificationStep;
import earlybird.earlybird.scheduler.notification.domain.NotificationUpdateType;
import earlybird.earlybird.scheduler.notification.service.NotificationInfoFactory;
import earlybird.earlybird.scheduler.notification.service.deregister.DeregisterNotificationService;
import earlybird.earlybird.scheduler.notification.service.register.RegisterNotificationService;
import earlybird.earlybird.scheduler.notification.service.update.request.UpdateFcmMessageServiceRequest;
import earlybird.earlybird.scheduler.notification.service.update.response.UpdateFcmMessageServiceResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateNotificationServiceTest {

    @Mock
    private RegisterNotificationService registerNotificationService;

    @Mock
    private DeregisterNotificationService deregisterNotificationService;

    @Mock
    private FindAppointmentService findAppointmentService;

    @Mock
    private NotificationInfoFactory notificationInfoFactory;

    @InjectMocks
    private UpdateNotificationService updateNotificationService;

    private Appointment appointment;
    private Long appointmentId;
    private NotificationUpdateType updateType;
    private final HashMap<NotificationStep, Instant> notificationInfo = new HashMap<>();

    @BeforeEach
    void setUp() {
        this.appointment = CreateTestAppointment.create();
        this.appointmentId = 1L;
        this.updateType = NotificationUpdateType.MODIFY;

        when(findAppointmentService.findBy(appointmentId, appointment.getClientId())).thenReturn(appointment);
        when(notificationInfoFactory.createTargetTimeMap(any(LocalDateTime.class), any(), any())).thenReturn(notificationInfo);
    }

    @DisplayName("업데이트를 하면 등록되어 있던 알림을 해제하고 새로운 알림을 등록한다.")
    @Test
    void updateThenDeregisterAndRegisterNotification() {
        // given

        UpdateFcmMessageServiceRequest request = UpdateFcmMessageServiceRequest.builder()
                .appointmentId(appointmentId)
                .appointmentName(appointment.getAppointmentName())
                .clientId(appointment.getClientId())
                .deviceToken(appointment.getDeviceToken())
                .preparationTime(LocalDateTime.now())
                .movingTime(LocalDateTime.now())
                .appointmentTime(LocalDateTime.now())
                .updateType(updateType)
                .build();


        // when
        updateNotificationService.update(request);

        // then
        verify(registerNotificationService).register(appointment, notificationInfo);
        verify(deregisterNotificationService).deregister(any());
    }

    @DisplayName("디바이스 토큰이 저장되어 있는 값과 업데이트시 요청 값이 다르면 업데이트한다.")
    @Test
    void changeDeviceTokenWhenRequestDeviceTokenIsDifferent() {
        // given
        String newDeviceToken = "newDeviceToken";

        UpdateFcmMessageServiceRequest request = UpdateFcmMessageServiceRequest.builder()
                .appointmentId(appointmentId)
                .appointmentName(appointment.getAppointmentName())
                .clientId(appointment.getClientId())
                .deviceToken(newDeviceToken)
                .preparationTime(LocalDateTime.now())
                .movingTime(LocalDateTime.now())
                .appointmentTime(LocalDateTime.now())
                .updateType(updateType)
                .build();

        // when
        UpdateFcmMessageServiceResponse response = updateNotificationService.update(request);

        // then
        assertThat(appointment.getDeviceToken()).isEqualTo(newDeviceToken);
        assertThat(response.getAppointment().getDeviceToken()).isEqualTo(newDeviceToken);
    }

    @DisplayName("약속 이름이 저장되어 있는 값과 업데이트시 요청 값이 다르면 업데이트한다.")
    @Test
    void changeAppointmentNameWhenRequestDeviceTokenIsDifferent() {
        // given
        String newAppointmentName = "newAppointmentName";

        UpdateFcmMessageServiceRequest request = UpdateFcmMessageServiceRequest.builder()
                .appointmentId(appointmentId)
                .appointmentName(newAppointmentName)
                .clientId(appointment.getClientId())
                .deviceToken(appointment.getDeviceToken())
                .preparationTime(LocalDateTime.now())
                .movingTime(LocalDateTime.now())
                .appointmentTime(LocalDateTime.now())
                .updateType(updateType)
                .build();

        // when
        UpdateFcmMessageServiceResponse response = updateNotificationService.update(request);

        // then
        
    }
}