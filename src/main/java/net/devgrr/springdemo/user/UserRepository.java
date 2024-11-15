package net.devgrr.springdemo.user;

import java.util.Optional;
import net.devgrr.springdemo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUserId(String userId);

  Optional<User> findByEmail(String email);
}
