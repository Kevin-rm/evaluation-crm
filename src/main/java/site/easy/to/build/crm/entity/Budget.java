package site.easy.to.build.crm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@DynamicInsert
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String description;

    @Setter
    @Column(nullable = false)
    private Double amount;

    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate startDate;

    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate endDate;

    private LocalDateTime createdAt;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;
}
