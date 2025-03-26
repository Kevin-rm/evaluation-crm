package site.easy.to.build.crm.projection;

import java.math.BigDecimal;

public interface TotalAmountByCustomer {
    String getCustomerName();

    BigDecimal getTotalAmount();
}
