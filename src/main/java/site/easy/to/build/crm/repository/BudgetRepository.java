package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.easy.to.build.crm.entity.Budget;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    List<Budget> findByCustomerCustomerId(Integer customerId);

    @Query(value = """
        SELECT b.customer_id,
               b.initialAmount,
               b.initialAmount - COALESCE((
                    SELECT SUM(amount)
                    FROM expense
                    WHERE customer_id = :customerId), 0) AS currentAmount
        FROM (SELECT customer_id, SUM(amount) AS initialAmount
              FROM budget
              WHERE customer_id = :customerId
              GROUP BY customer_id) b
    """, nativeQuery = true)
    Object getCustomerBudgetSummary(Integer customerId);
}
