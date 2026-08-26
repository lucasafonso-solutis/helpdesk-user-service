package solutis.lucas.afonso.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import solutis.lucas.afonso.helpdesk.entities.User;


public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
}
