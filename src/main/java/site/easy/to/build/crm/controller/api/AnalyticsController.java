package site.easy.to.build.crm.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.BudgetService;
import site.easy.to.build.crm.service.ExpenseService;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

// Data for chart

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5000")
public class AnalyticsController {
    
    private final ExpenseService expenseService;
    private final BudgetService budgetService;
    private final CustomerService customerService;

    @Autowired
    public AnalyticsController(ExpenseService expenseService, BudgetService budgetService, CustomerService customerService) {
        this.expenseService = expenseService;
        this.budgetService = budgetService;
        this.customerService = customerService;
    }

    @GetMapping("/expenses/tickets/by-customer")
    public Map<String, Double> getTicketExpensesByCustomer() {
        Map<Integer,Double> map = expenseService.getTicketExpensesByCustomer();
        Map<String, Double> expenses = new HashMap<>();
        for(Map.Entry<Integer,Double> entry : map.entrySet()){
            Customer customer  = customerService.findByCustomerId(entry.getKey());
            expenses.put(customer.getName(),entry.getValue());
        }
        return expenses;
    }

    @GetMapping("/expenses/leads/by-customer")
    public Map<String, Double> getLeadExpensesByCustomer() {
        Map<Integer,Double> map = expenseService.getLeadExpensesByCustomer();
        Map<String, Double> expenses = new HashMap<>();
        for(Map.Entry<Integer,Double> entry : map.entrySet()){
            Customer customer  = customerService.findByCustomerId(entry.getKey());
            expenses.put(customer.getName(),entry.getValue());
        }
        return expenses;
    }

    @GetMapping("/budgets/by-customer")
    public Map<String, Double> getBudgetsByCustomer() {
        Map<Integer,Double> map = budgetService.getBudgetsByCustomer();
        Map<String, Double> budgets = new HashMap<>();
        for(Map.Entry<Integer,Double> entry : map.entrySet()){
            Customer customer  = customerService.findByCustomerId(entry.getKey());
            budgets.put(customer.getName(),entry.getValue());
        }
        return budgets;
    }

    @GetMapping("/expenses/tickets/total")
    public BigDecimal getTotalTicketExpenses() {
        return expenseService.getTotalTicketExpenses();
    }

    @GetMapping("/expenses/leads/total")
    public BigDecimal getTotalLeadExpenses() {
        return expenseService.getTotalLeadExpenses();
    }

    @GetMapping("/budgets/total")
    public BigDecimal getTotalBudget() {
        return budgetService.getTotalBudget();
    }
}