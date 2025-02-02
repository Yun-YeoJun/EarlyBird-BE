package earlybird.earlybird.user.repository;

import earlybird.earlybird.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * findByAccountId와 findByUsername 함수는 서로 동일하지만 이름만 다른 함수<br>
 * 스프링 시큐리티에서는 accountId 대신 username 이라는 이름을 사용하기 때문에 가독성을 위해 이름이 다른 두 함수를 만들었음
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByAccountId(String accountId);

    @Query("select u from User u where u.accountId=:username")
    Optional<User> findByUsername(String username);
}
