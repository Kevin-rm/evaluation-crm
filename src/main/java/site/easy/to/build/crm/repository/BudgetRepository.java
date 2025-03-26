package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.projection.TotalAmountByCustomer;

import java.math.BigDecimal;
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

    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Budget b")
    BigDecimal totalBudget();

    @Query("""
        SELECT b.customer.name AS customerName, COALESCE(SUM(b.amount), 0) AS totalAmount
        FROM Budget b 
        GROUP BY b.customer.customerId
    """)
    List<TotalAmountByCustomer> getBudgetsGroupByCustomer();
}
