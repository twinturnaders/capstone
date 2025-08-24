package wgu.edu.BrinaBright.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import wgu.edu.BrinaBright.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);


        boolean existsByEmail(String email);
    }
