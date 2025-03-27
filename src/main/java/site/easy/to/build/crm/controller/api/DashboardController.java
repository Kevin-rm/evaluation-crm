package site.easy.to.build.crm.controller.api;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.DTO.DashboardData;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.budget.ExpenseService;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class DashboardController {

    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final CustomerService customerService;


    @GetMapping("/dashboard")
    public DashboardData getData() {
        return new DashboardData(
            expenseService.getTicketExpensesByCustomer(), 
            expenseService.getLeadExpensesByCustomer(), 
            budgetService.getBudgetsByCustomer(),
            expenseService.getTotalTicketExpenses().doubleValue(), 
            expenseService.getTotalLeadExpenses().doubleValue(), budgetService.getTotalBudget().doubleValue());
    }
}