package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

public interface FirebaseMessagingService {
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    void send(SendMessageByTokenServiceRequest request) throws FirebaseMessagingException;

    @Recover
    void recover(SendMessageByTokenServiceRequest request);
}
