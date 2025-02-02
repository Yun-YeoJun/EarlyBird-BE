package earlybird.earlybird.scheduler.notification.service;

import static earlybird.earlybird.scheduler.notification.domain.NotificationStep.*;
import static earlybird.earlybird.scheduler.notification.domain.NotificationStep.APPOINTMENT_TIME;

import earlybird.earlybird.scheduler.notification.domain.NotificationStep;

import lombok.Getter;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class NotificationInfoFactory {
    @Getter private final List<NotificationStep> notificationStepList;

    public NotificationInfoFactory() {
        this.notificationStepList =
                List.of(
                        ONE_HOUR_BEFORE_PREPARATION_TIME,
                        FIVE_MINUTES_BEFORE_PREPARATION_TIME,
                        PREPARATION_TIME,
                        TEN_MINUTES_BEFORE_MOVING_TIME,
                        MOVING_TIME,
                        FIVE_MINUTES_BEFORE_APPOINTMENT_TIME,
                        APPOINTMENT_TIME);
    }

    public Map<NotificationStep, Instant> createTargetTimeMap(
            Instant preparationTimeInstant,
            Instant movingTimeInstant,
            Instant appointmentTimeInstant) {
        return Map.of(
                ONE_HOUR_BEFORE_PREPARATION_TIME, preparationTimeInstant.minus(1, ChronoUnit.HOURS),
                FIVE_MINUTES_BEFORE_PREPARATION_TIME,
                        preparationTimeInstant.minus(5, ChronoUnit.MINUTES),
                PREPARATION_TIME, preparationTimeInstant,
                TEN_MINUTES_BEFORE_MOVING_TIME, movingTimeInstant.minus(10, ChronoUnit.MINUTES),
                MOVING_TIME, movingTimeInstant,
                FIVE_MINUTES_BEFORE_APPOINTMENT_TIME,
                        appointmentTimeInstant.minus(5, ChronoUnit.MINUTES),
                APPOINTMENT_TIME, appointmentTimeInstant);
    }

    public Map<NotificationStep, Instant> createTargetTimeMap(
            LocalDateTime preparationTime,
            LocalDateTime movingTime,
            LocalDateTime appointmentTime) {
        ZoneId seoul = ZoneId.of("Asia/Seoul");
        Instant preparationTimeInstant = preparationTime.atZone(seoul).toInstant();
        Instant movingTimeInstant = movingTime.atZone(seoul).toInstant();
        Instant appointmentTimeInstant = appointmentTime.atZone(seoul).toInstant();

        return this.createTargetTimeMap(
                preparationTimeInstant, movingTimeInstant, appointmentTimeInstant);
    }
}
