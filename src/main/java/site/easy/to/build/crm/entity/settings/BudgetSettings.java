package site.easy.to.build.crm.entity.settings;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@Entity
@DynamicInsert
public class BudgetSettings {
    public static final BigDecimal DEFAULT_ALERT_THRESHOLD = new BigDecimal("80.0");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @NotNull(message = "Alert threshold is required")
    @Positive(message = "Alert threshold must be positive")
    @DecimalMax(value = "100.0", message = "Alert threshold must be less than or equal to 100")
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal alertThreshold;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static BudgetSettings createDefault() {
        BudgetSettings budgetSettings = new BudgetSettings();
        budgetSettings.setAlertThreshold(DEFAULT_ALERT_THRESHOLD);

        return budgetSettings;
    }
}
