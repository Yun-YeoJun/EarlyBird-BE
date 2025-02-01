//package earlybird.earlybird.appointment.domain;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Profile;
//import org.springframework.test.context.ActiveProfiles;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
//import java.time.DayOfWeek;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Profile({"local"})
//@SpringBootTest
//class AppointmentTest {
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    @Autowired
//    private RepeatingDayRepository repeatingDayRepository;
//
//    @Autowired
//    private DataSource dataSource;
//
//    @BeforeEach
//    void setUp() {
//        Appointment appointment = Appointment.builder()
//                .appointmentTime(LocalTime.now())
//                .appointmentName("name")
//                .deviceToken("token")
//                .repeatingDayOfWeeks(List.of(DayOfWeek.MONDAY))
//                .preparationDuration(Duration.ZERO)
//                .movingDuration(Duration.ZERO)
//                .clientId("clientId")
//                .build();
//        Appointment savedAppointment = appointmentRepository.save(appointment);
//        appointment.getRepeatingDays().get(0).setDeleted();
//    }
//
//    @DisplayName("")
//    @Test
//    void test() throws SQLException {
//        // given
//        System.out.println(dataSource.getConnection().getMetaData().getURL());
//
////        RepeatingDay repeatingDay = savedAppointment.getRepeatingDays().get(0);
////        repeatingDay.setDeleted();
////        appointmentRepository.save(savedAppointment);
//        // when
//
//        List<Appointment> all = appointmentRepository.findAll();
////        List<RepeatingDay> allByDayOfWeek = repeatingDayRepository.findAllByDayOfWeek(DayOfWeek.MONDAY);
//        List<RepeatingDay> repeatingDays = all.get(0).getRepeatingDays();
//
//        List<RepeatingDay> all2 = repeatingDayRepository.findAll();
////        savedAppointment.setDeleted();
//        List<Appointment> all1 = appointmentRepository.findAll();
//        Assertions.assertThat("ddd").isEqualTo("d");
//
//        // then
//
//
//    }
//
//}