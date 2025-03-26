package site.easy.to.build.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.easy.to.build.crm.projection.TotalAmountByCustomer;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ExpenseByCustomer implements TotalAmountByCustomer {
    private String     customerName;
    private BigDecimal totalExpense;

    @Override
    public BigDecimal getTotalAmount() {
        return totalExpense;
    }
}
