package earlybird.earlybird.scheduler.notification.service.register;

import earlybird.earlybird.appointment.domain.Appointment;
import earlybird.earlybird.appointment.domain.AppointmentRepository;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

public class AppointmentRepositoryStub implements AppointmentRepository {
  @Override
  public void flush() {}

  @Override
  public <S extends Appointment> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends Appointment> List<S> saveAllAndFlush(Iterable<S> entities) {
    return List.of();
  }

  @Override
  public void deleteAllInBatch(Iterable<Appointment> entities) {}

  @Override
  public void deleteAllByIdInBatch(Iterable<Long> longs) {}

  @Override
  public void deleteAllInBatch() {}

  @Override
  public Appointment getOne(Long aLong) {
    return null;
  }

  @Override
  public Appointment getById(Long aLong) {
    return null;
  }

  @Override
  public Appointment getReferenceById(Long aLong) {
    return null;
  }

  @Override
  public <S extends Appointment> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Appointment> List<S> findAll(Example<S> example) {
    return List.of();
  }

  @Override
  public <S extends Appointment> List<S> findAll(Example<S> example, Sort sort) {
    return List.of();
  }

  @Override
  public <S extends Appointment> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Appointment> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends Appointment> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends Appointment, R> R findBy(
      Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public <S extends Appointment> S save(S entity) {
    return null;
  }

  @Override
  public <S extends Appointment> List<S> saveAll(Iterable<S> entities) {
    return List.of();
  }

  @Override
  public Optional<Appointment> findById(Long aLong) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public List<Appointment> findAll() {
    return List.of();
  }

  @Override
  public List<Appointment> findAllById(Iterable<Long> longs) {
    return List.of();
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(Long aLong) {}

  @Override
  public void delete(Appointment entity) {}

  @Override
  public void deleteAllById(Iterable<? extends Long> longs) {}

  @Override
  public void deleteAll(Iterable<? extends Appointment> entities) {}

  @Override
  public void deleteAll() {}

  @Override
  public List<Appointment> findAll(Sort sort) {
    return List.of();
  }

  @Override
  public Page<Appointment> findAll(Pageable pageable) {
    return null;
  }
}
