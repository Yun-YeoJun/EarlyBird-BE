package earlybird.earlybird.scheduler.notification.fcm.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SendMessageByTokenServiceRequest {
    private String title;
    private String body;
    private String deviceToken;
    private String uuid;

    @Builder
    private SendMessageByTokenServiceRequest(String title, String body, String deviceToken, String uuid) {
        this.title = title;
        this.body = body;
        this.deviceToken = deviceToken;
        this.uuid = uuid;
    }

//    public static SendMessageByTokenServiceRequest from(RegisterFcmMessageAtSchedulerServiceRequest request) {
//        return SendMessageByTokenServiceRequest.builder()
//                .title(request.getTitle())
//                .body(request.getBody())
//                .deviceToken(request.getDeviceToken())
//                .uuid(request.getUuid())
//                .build();
//    }

    public static SendMessageByTokenServiceRequest from(AddTaskToSchedulingTaskListServiceRequest request) {
        return SendMessageByTokenServiceRequest.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .deviceToken(request.getDeviceToken())
                .uuid(request.getUuid())
                .build();
    }
}
