package earlybird.earlybird.messaging;

import earlybird.earlybird.messaging.request.SendMessageByTokenServiceRequest;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

public interface MessagingService {
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    void send(SendMessageByTokenServiceRequest request);

    @Recover
    void recover(SendMessageByTokenServiceRequest request);
}
