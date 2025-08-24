package wgu.edu.BrinaBright.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import wgu.edu.BrinaBright.Entities.UserBill;

import java.util.List;

public interface UserBillRepository extends JpaRepository<UserBill, Long> {
    List<UserBill> findByUserId(Long id);


}