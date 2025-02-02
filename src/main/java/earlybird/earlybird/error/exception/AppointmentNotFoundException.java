package earlybird.earlybird.error.exception;

import earlybird.earlybird.error.ErrorCode;

public class AppointmentNotFoundException extends NotFoundException {
  public AppointmentNotFoundException() {
    super(ErrorCode.APPOINTMENT_NOT_FOUND);
  }
}
