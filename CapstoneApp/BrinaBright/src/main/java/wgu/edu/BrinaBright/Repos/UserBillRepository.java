package wgu.edu.BrinaBright.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wgu.edu.BrinaBright.DTOs.UserBillDTO;
import wgu.edu.BrinaBright.Entities.User;
import wgu.edu.BrinaBright.Entities.UserBill;

import java.util.List;
import java.util.Optional;

public interface UserBillRepository extends JpaRepository<UserBill, Long> {

    List<UserBill> findByUserIdOrderByBillDateDesc(Long userId);
    List<UserBillDTO> findByUserId(Long id);


    Optional<Object> findByIdAndUserId(Long billId, Long userId);

    Long id(Long id);

    List<UserBillDTO> findByUser(User user);
}
    


   