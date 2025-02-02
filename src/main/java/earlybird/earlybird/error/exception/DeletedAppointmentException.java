package earlybird.earlybird.error.exception;

import static earlybird.earlybird.error.ErrorCode.DELETED_APPOINTMENT_EXCEPTION;

public class DeletedAppointmentException extends BusinessBaseException {
    public DeletedAppointmentException() {
        super(DELETED_APPOINTMENT_EXCEPTION);
    }
}
