package site.easy.to.build.crm.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.response.ApiResponse;
import site.easy.to.build.crm.service.ExpenseService;
import site.easy.to.build.crm.service.lead.LeadService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/leads")
public class LeadApiController {
    private final LeadService leadService;
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<Lead>> getAll() {
        return ResponseEntity.ok(leadService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        Lead lead = leadService.findByLeadId(id);
        if (lead == null) return ApiResponse.notFound("Lead not found").toResponseEntity();

        leadService.delete(lead);
        expenseService.delete(lead.getExpense());

        return ApiResponse.success(String.format("Lead with id %d deleted successfully", id))
            .toResponseEntity();
    }
}
