package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import site.easy.to.build.crm.entity.Budget;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    List<Budget> findByCustomerCustomerId(Integer customerId);

    @Query(value = """
            SELECT
                b.initialAmount,
                (b.initialAmount - COALESCE(e.totalExpense, 0)) AS currentAmount,
                b.customer_id
            FROM
                (SELECT customer_id, SUM(amount) AS initialAmount
                 FROM budget
                 WHERE customer_id = :customerId
                 GROUP BY customer_id) b
                    LEFT JOIN
                (SELECT customer_id, SUM(amount) AS totalExpense
                 FROM crm.expense
                 WHERE customer_id = :customerId
                 GROUP BY customer_id) e
                ON b.customer_id = e.customer_id
            """, nativeQuery = true)
    Object getBudgetsAfterExpenseRawGlobal(@Param("customerId") Integer customerId);

//    @Query(value = """
//            SELECT
//                    SUM(b.amount)
//            FROM budget b
//            WHERE b.customer_id = :customerId
//            """, nativeQuery = true)
//    Object getBudgetsAfterExpenseRawGlobal(@Param("customerId") Integer customerId);


}
