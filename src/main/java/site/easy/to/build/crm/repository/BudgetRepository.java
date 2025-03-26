package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.projection.TotalAmountByCustomer;

import java.math.BigDecimal;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Integer> {

    @Query("""
                select COALESCE(SUM(b.amount),0)  from Budget b where b.customer.customerId = :customerId
            """)
    public double getTotalBudgetByCustomer(@Param("customerId") Integer customerId);

//    @Query(value = """
//            SELECT
//                    b.budget_id,
//                    b.title,
//                    b.amount,
//                    b.amount - COALESCE(SUM(e.amount), 0),
//                    b.start_date,
//                    b.end_date,
//                    b.customer_id
//            FROM budget b
//            LEFT JOIN crm.expense e ON b.budget_id = e.budget_id
//            WHERE b.customer_id = :customerId
//            GROUP BY b.budget_id, b.title, b.start_date, b.end_date, b.customer_id,b.amount
//            """, nativeQuery = true)
//    List<Object[]> getBudgetsAfterExpenseRaw(@Param("customerId") Integer customerId);


    @Query("SELECT COALESCE(SUM(b.amount), 0) FROM Budget b")
    BigDecimal totalBudget();

    @Query("""
        SELECT b.customer.name AS customerName, COALESCE(SUM(b.amount), 0) AS totalAmount
        FROM Budget b 
        GROUP BY b.customer.customerId
    """)
    List<TotalAmountByCustomer> getBudgetsGroupByCustomer();
}
