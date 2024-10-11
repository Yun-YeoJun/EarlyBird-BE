package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.error.exception.FcmMessageTimeBeforeNowException;
import earlybird.earlybird.scheduler.notification.fcm.domain.FcmNotificationRepository;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.request.RegisterFcmMessageAtSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.fcm.service.response.RegisterFcmMessageAtSchedulerServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterNotificationAtSchedulerService {

    private final TaskScheduler taskScheduler;
    private final SendMessageToFcmService sendMessageToFcmService;
    private final FcmNotificationRepository fcmNotificationRepository;

    @Transactional
    public RegisterFcmMessageAtSchedulerServiceResponse registerFcmMessage(RegisterFcmMessageAtSchedulerServiceRequest request) {
        Instant targetTime = request.getTargetTimeInstant();

        SendMessageByTokenServiceRequest sendMessageByTokenServiceRequest = SendMessageByTokenServiceRequest.from(request);

        if (targetTime.isBefore(Instant.now())) {
            throw new FcmMessageTimeBeforeNowException();
        }

        taskScheduler.schedule(() -> {
            try {
                sendMessageToFcmService.sendMessageByToken(sendMessageByTokenServiceRequest);
            } catch (FirebaseMessagingException e) {
                log.error(e.getMessage());
            }
        }, targetTime);

        return RegisterFcmMessageAtSchedulerServiceResponse.of(fcmNotificationRepository.save(request.toFcmNotification()));
    }
}
