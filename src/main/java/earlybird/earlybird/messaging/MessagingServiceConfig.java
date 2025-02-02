package earlybird.earlybird.messaging;

import earlybird.earlybird.messaging.firebase.FirebaseMessagingService;
import earlybird.earlybird.messaging.firebase.FirebaseMessagingServiceProxy;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class MessagingServiceConfig {

  @Bean
  public MessagingService messagingService(
      FcmNotificationRepository repository,
      @Qualifier("taskExecutor") ThreadPoolTaskExecutor executor) {
    return new FirebaseMessagingServiceProxy(
        firebaseMessagingService(repository), repository, executor);
  }

  @Bean
  public FirebaseMessagingService firebaseMessagingService(FcmNotificationRepository repository) {
    return new FirebaseMessagingService(repository);
  }
}
