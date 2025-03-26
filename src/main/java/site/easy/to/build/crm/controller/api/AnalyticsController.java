package site.easy.to.build.crm.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.projection.TicketAndLeadExpenseTotals;
import site.easy.to.build.crm.response.ApiResponse;
import site.easy.to.build.crm.service.BudgetService;
import site.easy.to.build.crm.service.ExpenseService;

import java.math.BigDecimal;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final ExpenseService expenseService;
    private final BudgetService budgetService;

    @GetMapping("/total-data")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> totalData() {
        TicketAndLeadExpenseTotals ticketAndLeadExpenseTotals = expenseService.expenseSumsForTicketAndLead();

        return ApiResponse.success(Map.of(
            "ticketExpenseTotals", ticketAndLeadExpenseTotals.getTicketExpenses(),
            "leadExpenseTotals", ticketAndLeadExpenseTotals.getLeadExpenses(),
            "totalBudget", budgetService.getTotalBudget()
        )).toResponseEntity();
    }
}