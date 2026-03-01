package observatoriodeviolencia.msauth.repository;

import observatoriodeviolencia.msauth.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findByMail(String mail);
}
