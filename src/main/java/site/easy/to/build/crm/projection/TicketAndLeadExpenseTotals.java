package site.easy.to.build.crm.projection;

import java.math.BigDecimal;

public interface TicketAndLeadExpenseTotals {
    BigDecimal getTicketExpenses();

    BigDecimal getLeadExpenses();
}
