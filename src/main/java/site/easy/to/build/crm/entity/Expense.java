package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@ToString
@Entity
@DynamicInsert
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description")
    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères.")
    private String description;

    @Column(name = "amount")
    @Positive(message = "Amount must be positive.")
    private Double amount;

    @Setter
    @NotNull(message = "Expense date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate expenseDate;

}
