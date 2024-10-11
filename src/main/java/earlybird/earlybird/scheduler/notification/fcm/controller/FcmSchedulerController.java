package earlybird.earlybird.scheduler.notification.fcm.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.scheduler.notification.fcm.controller.request.DeregisterNotificationByTokenRequest;
import earlybird.earlybird.scheduler.notification.fcm.controller.request.RegisterNotificationByTokenRequest;
import earlybird.earlybird.scheduler.notification.fcm.controller.request.UpdateNotificationRequest;
import earlybird.earlybird.scheduler.notification.fcm.controller.response.RegisterNotificationByTokenResponse;
import earlybird.earlybird.scheduler.notification.fcm.controller.response.UpdateNotificationResponse;
import earlybird.earlybird.scheduler.notification.fcm.service.DeregisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.fcm.service.RegisterNotificationAtSchedulerService;
import earlybird.earlybird.scheduler.notification.fcm.service.UpdateNotificationService;
import earlybird.earlybird.scheduler.notification.fcm.service.response.RegisterFcmMessageAtSchedulerServiceResponse;
import earlybird.earlybird.scheduler.notification.fcm.service.response.UpdateFcmMessageServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/message/fcm/token")
@RestController
public class FcmSchedulerController {

    private final RegisterNotificationAtSchedulerService registerService;
    private final DeregisterNotificationAtSchedulerService deregisterService;
    private final UpdateNotificationService updateService;

    @PostMapping
    public ResponseEntity<?> registerTokenNotification(@Valid @RequestBody RegisterNotificationByTokenRequest request) throws FirebaseMessagingException {
        RegisterFcmMessageAtSchedulerServiceResponse serviceResponse = registerService
                .registerFcmMessage(request.toRegisterFcmMessageAtSchedulerRequest());

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
    public ResponseEntity<?> deregisterTokenNotificationAtScheduler(@Valid @RequestBody DeregisterNotificationByTokenRequest request) {
        deregisterService.deregister(request.toServiceRequest());
        return ResponseEntity.noContent().build();
    }
}
