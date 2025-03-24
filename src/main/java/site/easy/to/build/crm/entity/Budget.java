package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;
import site.easy.to.build.crm.customValidations.budget.StartDateBeforeEndDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@StartDateBeforeEndDate
@Entity
@DynamicInsert
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @NotBlank(message = "Budget name is required")
    @Size(min = 3, max = 255, message = "Name must contain between 3 and 255 characters")
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String description;

    @Setter
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    @Column(nullable = false)
    private Double amount;

    @Setter
    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate startDate;

    @Setter
    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate endDate;

    private LocalDateTime createdAt;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;
}
