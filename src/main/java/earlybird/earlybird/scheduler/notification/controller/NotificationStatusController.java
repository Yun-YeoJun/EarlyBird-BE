package earlybird.earlybird.scheduler.notification.controller;

import earlybird.earlybird.scheduler.notification.service.update.UpdateNotificationStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/notification/status")
@RestController
public class NotificationStatusController {

  private final UpdateNotificationStatusService updateNotificationStatusService;

  // 쿼리 파라미터 이름 수정할 경우 AWS 람다 함수에서도 수정해야함
  @PostMapping
  public ResponseEntity<?> setNotificationStatus(
      @RequestParam Long notificationId, @RequestParam Boolean sendSuccess) {
    updateNotificationStatusService.update(notificationId, sendSuccess);
    return ResponseEntity.ok().build();
  }
}
