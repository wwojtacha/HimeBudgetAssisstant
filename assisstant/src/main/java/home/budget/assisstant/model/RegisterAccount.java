package home.budget.assisstant.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "balance")
public class RegisterAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required.")
    @Column
    private String name;

    @NotNull
    @Column
    private BigDecimal balance = BigDecimal.ZERO;



}
