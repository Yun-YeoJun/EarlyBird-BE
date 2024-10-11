package earlybird.earlybird.scheduler.notification.fcm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import earlybird.earlybird.scheduler.notification.fcm.service.request.SendMessageByTokenServiceRequest;

/**
 * 테스트 코드 작성 시 firebase messaging 관련 mocking 을 위해 인터페이스를 정의함
 * -> 테스트코드에서 DI로 주입
 */
public interface FirebaseMessagingService {
    String send(SendMessageByTokenServiceRequest request) throws FirebaseMessagingException;
}
