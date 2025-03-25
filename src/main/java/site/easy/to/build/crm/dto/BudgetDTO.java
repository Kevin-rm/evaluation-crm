package site.easy.to.build.crm.dto;

import lombok.*;

import java.time.LocalDate;

@Data
public class BudgetDTO {
    private Integer budgetId;
    private String title;
    private Double initialAmount;
    private Double currentAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer customerId;
    private String status;
}
