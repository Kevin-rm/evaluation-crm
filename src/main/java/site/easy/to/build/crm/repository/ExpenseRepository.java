package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.easy.to.build.crm.dto.ExpenseByCustomer;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.projection.TicketAndLeadExpenseTotals;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    @Query(value = """
        SELECT (SELECT COALESCE(SUM(e.amount), 0) 
                FROM expense e 
                    JOIN trigger_ticket tt ON e.id = tt.expense_id
                WHERE tt.expense_id IS NOT NULL) AS ticketExpenses,
               (SELECT COALESCE(SUM(e.amount), 0) 
                FROM expense e
                    JOIN trigger_lead tl ON e.id = tl.expense_id
                WHERE tl.expense_id IS NOT NULL) AS leadExpenses
    """, nativeQuery = true)
    TicketAndLeadExpenseTotals expenseSumsForTicketAndLead();

    @Query("""
        SELECT NEW site.easy.to.build.crm.dto.ExpenseByCustomer(
             c.name, 
             CAST(COALESCE(SUM(t.expense.amount), 0.0) AS BigDecimal)
        ) FROM Ticket t
            JOIN t.customer c
        GROUP BY c.name, c.customerId
    """)
    List<ExpenseByCustomer> findTicketExpensesGroupByCustomer();

    @Query("""
        SELECT NEW site.easy.to.build.crm.dto.ExpenseByCustomer(
             c.name, 
             CAST(COALESCE(SUM(l.expense.amount), 0.0) AS BigDecimal)
        ) FROM Lead l
            JOIN l.customer c
        GROUP BY c.name, c.customerId
    """)
    List<ExpenseByCustomer> findLeadExpensesGroupByCustomer();
}
