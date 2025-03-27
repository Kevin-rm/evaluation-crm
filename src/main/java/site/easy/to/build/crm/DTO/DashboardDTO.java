package site.easy.to.build.crm.DTO;

import java.util.Map;

public record DashboardData(
    Map<String, Double> ticketExpensesByCustomer;
    Map<String, Double> leadExpensesByCustomer;
    Map<String, Double> budgetByCustomer;
    Double totalTicketExpenses;
    Double totalLeadExpenses;
    Double totalBudget;
) { }
