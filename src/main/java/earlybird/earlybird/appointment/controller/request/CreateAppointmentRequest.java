package earlybird.earlybird.appointment.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import earlybird.earlybird.appointment.service.request.CreateAppointmentServiceRequest;
import earlybird.earlybird.common.DayOfWeekUtil;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CreateAppointmentRequest {
    @NotBlank private String clientId;
    @NotBlank private String deviceToken;
    @NotBlank private String appointmentName;
    @NotNull private Duration movingDuration;
    @NotNull private Duration preparationDuration;

    @NotNull
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "Asia/Seoul")
    private LocalDateTime firstAppointmentTime;

    // 월화수목금토일
    @NotNull
    @Size(min = 7, max = 7, message = "repeatDayOfWeek 의 길이는 7이어야 합니다.")
    private List<Boolean> repeatDayOfWeekBoolList;

    public CreateAppointmentServiceRequest toCreateAppointmentServiceRequest() {

        List<DayOfWeek> repeatDayOfWeekList =
                DayOfWeekUtil.convertBooleanListToDayOfWeekList(this.repeatDayOfWeekBoolList);

        return CreateAppointmentServiceRequest.builder()
                .clientId(this.clientId)
                .deviceToken(this.deviceToken)
                .appointmentName(this.appointmentName)
                .firstAppointmentTime(this.firstAppointmentTime)
                .preparationDuration(this.preparationDuration)
                .movingDuration(this.movingDuration)
                .repeatDayOfWeekList(repeatDayOfWeekList)
                .build();
    }
}
