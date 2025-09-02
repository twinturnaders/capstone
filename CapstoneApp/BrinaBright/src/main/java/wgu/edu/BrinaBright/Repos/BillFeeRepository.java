package wgu.edu.BrinaBright.Repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wgu.edu.BrinaBright.Entities.BillFee;
import wgu.edu.BrinaBright.Entities.UserBill;

import java.util.Collection;
import java.util.List;

public interface BillFeeRepository extends JpaRepository<BillFee, Long> {
    List<BillFee> findByUserBillIdIn(Collection<Long> billIds);

    @Query("""
       select
         f.userBill.id as billId,
         f.feeType    as name,
         f.feeAmount  as amount
       from BillFee f
       where f.userBill.id in :billIds
    """)
    List<FeeRow> findRowsByUserBillIdIn(@Param("billIds") Collection<Long> billIds);
}


