package earlybird.earlybird.scheduler.manager.aws;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStatus.PENDING;

import earlybird.earlybird.common.LocalDateTimeUtil;
import earlybird.earlybird.error.exception.FcmNotificationNotFoundException;
import earlybird.earlybird.scheduler.manager.NotificationSchedulerManager;
import earlybird.earlybird.scheduler.manager.request.AddNotificationToSchedulerServiceRequest;
import earlybird.earlybird.scheduler.notification.domain.FcmNotification;
import earlybird.earlybird.scheduler.notification.domain.FcmNotificationRepository;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AwsNotificationSchedulerManagerService implements NotificationSchedulerManager {
    private final DynamoDbClientFactory dynamoDbClientFactory;
    private final String tableName = "earlybird-notification";
    private final String partitionKey = "notificationId";
    private final String sortKey = "targetTime";
    private final FcmNotificationRepository fcmNotificationRepository;
    private final DateTimeFormatter targetTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AwsNotificationSchedulerManagerService(
            DynamoDbClientFactory dynamoDbClientFactory,
            FcmNotificationRepository fcmNotificationRepository) {
        this.dynamoDbClientFactory = dynamoDbClientFactory;
        this.fcmNotificationRepository = fcmNotificationRepository;
    }

    @Override
    @PostConstruct
    public void init() {
        fcmNotificationRepository.findAllByStatusIs(PENDING).stream()
                .filter(
                        notification ->
                                notification
                                        .getTargetTime()
                                        .isAfter(LocalDateTimeUtil.getLocalDateTimeNow()))
                .map(AddNotificationToSchedulerServiceRequest::of)
                .forEach(this::add);
    }

    @Override
    @Transactional
    public void add(AddNotificationToSchedulerServiceRequest request) {
        Map<String, AttributeValue> notificationInfo = createNotificationAttributeMap(request);

        try (DynamoDbClient dynamoDbClient = dynamoDbClientFactory.create()) {
            dynamoDbClient.putItem(
                    PutItemRequest.builder()
                            .tableName(this.tableName)
                            .item(notificationInfo)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void remove(Long notificationId) {
        Map<String, AttributeValue> key = createNotificationAttributeMap(notificationId);
        try (DynamoDbClient dynamoDbClient = dynamoDbClientFactory.create()) {
            dynamoDbClient.deleteItem(
                    DeleteItemRequest.builder().tableName(tableName).key(key).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, AttributeValue> createNotificationAttributeMap(Long notificationId) {
        FcmNotification notification =
                fcmNotificationRepository
                        .findById(notificationId)
                        .orElseThrow(FcmNotificationNotFoundException::new);
        String targetTime = notification.getTargetTime().format(targetTimeFormatter);
        String notificationIdStr = String.valueOf(notificationId);

        Map<String, AttributeValue> key = new HashMap<>();
        key.put(this.partitionKey, AttributeValue.builder().s(notificationIdStr).build());
        key.put(this.sortKey, AttributeValue.builder().s(targetTime).build());
        return key;
    }

    private Map<String, AttributeValue> createNotificationAttributeMap(
            AddNotificationToSchedulerServiceRequest request) {
        String notificationTitle = request.getNotificationStep().getTitle();
        String notificationBody = request.getNotificationStep().getBody();
        String targetTime =
                LocalDateTime.ofInstant(request.getTargetTime(), ZoneId.of("Asia/Seoul"))
                        .format(targetTimeFormatter);
        String deviceToken = request.getDeviceToken();
        String notificationId = String.valueOf(request.getNotificationId());

        Map<String, AttributeValue> notificationInfo = new HashMap<>();
        notificationInfo.put("notificationTitle", createStringAttributeValue(notificationTitle));
        notificationInfo.put("notificationBody", createStringAttributeValue(notificationBody));
        notificationInfo.put(this.sortKey, createStringAttributeValue(targetTime));
        notificationInfo.put("deviceToken", createStringAttributeValue(deviceToken));
        notificationInfo.put(this.partitionKey, createStringAttributeValue(notificationId));

        return notificationInfo;
    }

    private AttributeValue createStringAttributeValue(String value) {
        return AttributeValue.builder().s(value).build();
    }
}
