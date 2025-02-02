package earlybird.earlybird.scheduler.notification.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

public class FcmNotificationRepositoryStub implements FcmNotificationRepository {

  private final List<FcmNotification> fcmNotifications = new ArrayList<>();

  @Override
  public Optional<FcmNotification> findByIdAndStatusForUpdate(Long id, NotificationStatus status) {
    return Optional.empty();
  }

  @Override
  public List<FcmNotification> findAllByStatusIs(NotificationStatus status) {
    return List.of();
  }

  @Override
  public void flush() {}

  @Override
  public <S extends FcmNotification> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends FcmNotification> List<S> saveAllAndFlush(Iterable<S> entities) {
    return List.of();
  }

  @Override
  public void deleteAllInBatch(Iterable<FcmNotification> entities) {}

  @Override
  public void deleteAllByIdInBatch(Iterable<Long> longs) {}

  @Override
  public void deleteAllInBatch() {}

  @Override
  public FcmNotification getOne(Long aLong) {
    return null;
  }

  @Override
  public FcmNotification getById(Long aLong) {
    return null;
  }

  @Override
  public FcmNotification getReferenceById(Long aLong) {
    return null;
  }

  @Override
  public <S extends FcmNotification> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends FcmNotification> List<S> findAll(Example<S> example) {
    return List.of();
  }

  @Override
  public <S extends FcmNotification> List<S> findAll(Example<S> example, Sort sort) {
    return List.of();
  }

  @Override
  public <S extends FcmNotification> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends FcmNotification> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends FcmNotification> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends FcmNotification, R> R findBy(
      Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  @Override
  public <S extends FcmNotification> S save(S entity) {
    fcmNotifications.add(entity);
    return entity;
  }

  @Override
  public <S extends FcmNotification> List<S> saveAll(Iterable<S> entities) {
    return List.of();
  }

  @Override
  public Optional<FcmNotification> findById(Long aLong) {
    return fcmNotifications.stream()
        .filter(fcmNotification -> fcmNotification.getId().equals(aLong))
        .findFirst();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public List<FcmNotification> findAll() {
    return fcmNotifications;
  }

  @Override
  public List<FcmNotification> findAllById(Iterable<Long> longs) {
    return List.of();
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(Long aLong) {}

  @Override
  public void delete(FcmNotification entity) {}

  @Override
  public void deleteAllById(Iterable<? extends Long> longs) {}

  @Override
  public void deleteAll(Iterable<? extends FcmNotification> entities) {}

  @Override
  public void deleteAll() {}

  @Override
  public List<FcmNotification> findAll(Sort sort) {
    return List.of();
  }

  @Override
  public Page<FcmNotification> findAll(Pageable pageable) {
    return null;
  }
}
