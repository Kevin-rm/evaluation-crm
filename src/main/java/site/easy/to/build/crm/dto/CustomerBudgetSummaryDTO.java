package site.easy.to.build.crm.dto;

import lombok.*;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class CustomerBudgetSummaryDTO {
    private final Integer customerId;
    private final BigDecimal initialAmount;
    private final BigDecimal currentAmount;
    private final BigDecimal alertThreshold;
    @Setter
    private BudgetStatusAlert status = BudgetStatusAlert.OK;

    public BigDecimal totalExpenses() {
        Assert.notNull(initialAmount, "Initial amount must not be null");
        Assert.notNull(currentAmount, "Current amount must not be null");

        return initialAmount.subtract(currentAmount);
    }

    public enum BudgetStatusAlert { OK, WARNING }
}
