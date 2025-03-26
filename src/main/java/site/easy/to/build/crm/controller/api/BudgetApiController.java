package site.easy.to.build.crm.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.projection.TotalAmountByCustomer;
import site.easy.to.build.crm.response.ApiResponse;
import site.easy.to.build.crm.service.BudgetService;
import site.easy.to.build.crm.service.settings.BudgetSettingsService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/budgets")
public class BudgetApiController {

    private final BudgetService budgetService;
    private final BudgetSettingsService budgetSettingsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Budget>>> getAll() {
        return ApiResponse.success(budgetService.getAll()).toResponseEntity();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Budget>> getByCustomer(@PathVariable Integer customerId) {
        List<Budget> budget = budgetService.getByCustomer(customerId);
        return new ResponseEntity<>(budget, HttpStatus.OK);
    }

    @GetMapping("/by-customer")
    public ResponseEntity<ApiResponse<List<TotalAmountByCustomer>>> getBudgetsGroupByCustomer() {
        return ApiResponse.success(budgetService.getBudgetsGroupByCustomer()).toResponseEntity();
    }

    @GetMapping("/alert-threshold")
    public ResponseEntity<ApiResponse<Map<String, BigDecimal>>> getAlertThreshold() {
        return ApiResponse.success(Map.of("alert-threshold",
            budgetSettingsService.getDefault().getAlertThreshold()
        )).toResponseEntity();
    }

    @PutMapping("/alert-threshold")
    public ResponseEntity<ApiResponse<Object>> updateThresholdAlert(@RequestBody BigDecimal value) {
        budgetSettingsService.updateDefaultAlertThreshold(value);

        return ApiResponse.success("Alert threshold successfully updated").toResponseEntity();
    }
}
