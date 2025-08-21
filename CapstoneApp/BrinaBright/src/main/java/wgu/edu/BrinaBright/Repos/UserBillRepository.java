package wgu.edu.BrinaBright.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import wgu.edu.BrinaBright.Entities.UserBill;

public interface UserBillRepository extends JpaRepository<UserBill, Long> {}