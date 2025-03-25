package site.easy.to.build.crm.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.response.ApiResponse;
import site.easy.to.build.crm.service.ExpenseService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/expenses")
public class ExpenseApiController {
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Expense>>> getAll() {
        return ApiResponse.success(expenseService.getAll()).toResponseEntity();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> update(
        @PathVariable Integer id, @RequestBody Double newAmount
    ) {
        Optional<Expense> optionalExpense = expenseService.getById(id);
        if (optionalExpense.isEmpty())
            return ApiResponse.notFound("Expense not found").toResponseEntity();

        Expense expense = optionalExpense.get();
        expense.setAmount(newAmount);
        expenseService.save(expense);

        return ApiResponse.success(String.format("Expense with id %d updated successfully", id))
            .toResponseEntity();
    }
}
