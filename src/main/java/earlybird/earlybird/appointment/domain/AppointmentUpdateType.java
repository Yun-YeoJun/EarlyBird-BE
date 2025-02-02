package earlybird.earlybird.appointment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppointmentUpdateType {
  POSTPONE("약속 미루기"),
  MODIFY("일정 수정하기");

  private final String type;
}
