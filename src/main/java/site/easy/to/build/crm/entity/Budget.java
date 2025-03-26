package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
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

    @Column(name = "amount")
    @Positive(message = "Le montant doit être supérieur à 0.")
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

    @ManyToOne(optional = false )
    @JoinColumn(name = "customer_id")
    @NotNull(message = "Le client associé est obligatoire.")
    Customer customer;

    @JsonIgnore
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;
}
