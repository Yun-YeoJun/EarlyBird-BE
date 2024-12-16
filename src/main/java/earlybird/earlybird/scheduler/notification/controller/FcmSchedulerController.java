package earlybird.earlybird.scheduler.notification.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.scheduler.notification.controller.request.DeregisterNotificationByTokenRequest;
import earlybird.earlybird.scheduler.notification.controller.request.RegisterNotificationByTokenRequest;
import earlybird.earlybird.scheduler.notification.controller.request.UpdateNotificationRequest;
import earlybird.earlybird.scheduler.notification.controller.response.RegisterNotificationByTokenResponse;
import earlybird.earlybird.scheduler.notification.controller.response.UpdateNotificationResponse;
import earlybird.earlybird.scheduler.notification.service.deregister.DeregisterNotificationService;
import earlybird.earlybird.scheduler.notification.service.register.RegisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.service.register.response.RegisterNotificationServiceResponse;
import earlybird.earlybird.scheduler.notification.service.update.UpdateNotificationService;
import earlybird.earlybird.scheduler.notification.service.update.response.UpdateFcmMessageServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Deprecated:
 * 클라이언트에서 이 API 를 사용하는 코드가
 * 전부 AppointmentController 의 API 로 변경되면
 * 이 컨트롤러 삭제
 * TODO: 람다에서 전송 성공하거나 실패했을 때 호출할 컨트롤러 만들기
 */
@Deprecated
@RequiredArgsConstructor
@RequestMapping("/api/v1/message/fcm/token")
@RestController
public class FcmSchedulerController {

    private final RegisterNotificationAtSchedulerService registerService;
    private final DeregisterNotificationService deregisterService;
    private final UpdateNotificationService updateService;

    @PostMapping
    public ResponseEntity<?> registerTokenNotification(@Valid @RequestBody RegisterNotificationByTokenRequest request) throws FirebaseMessagingException {
        RegisterNotificationServiceResponse serviceResponse = registerService
                .registerFcmMessageForNewAppointment(request.toRegisterFcmMessageForNewAppointmentAtSchedulerRequest());

        RegisterNotificationByTokenResponse controllerResponse = RegisterNotificationByTokenResponse.from(serviceResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(controllerResponse);
    }

    @PatchMapping
    public ResponseEntity<?> updateNotification(@Valid @RequestBody UpdateNotificationRequest request) {
        UpdateFcmMessageServiceResponse serviceResponse = updateService.update(request.toServiceRequest());
        UpdateNotificationResponse controllerResponse = UpdateNotificationResponse.from(serviceResponse);

        return ResponseEntity
                .ok()
                .body(controllerResponse);
    }

    @DeleteMapping
    public ResponseEntity<?> deregisterTokenNotificationAtScheduler(
            @RequestHeader("appointmentId") Long appointmentId, @RequestHeader("clientId") String clientId) {

        DeregisterNotificationByTokenRequest request = DeregisterNotificationByTokenRequest.builder()
                .appointmentId(appointmentId)
                .clientId(clientId)
                .build();

        deregisterService.deregister(request.toServiceRequest());
        return ResponseEntity.noContent().build();
    }
}
