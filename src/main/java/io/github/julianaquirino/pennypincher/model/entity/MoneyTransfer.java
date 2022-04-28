package io.github.julianaquirino.pennypincher.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "money_transfer", schema = "public")
public class MoneyTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_record_debit")
    private Record recordDebit;

    @ManyToOne
    @JoinColumn(name = "id_record_credit")
    private Record recordCredit;

    @Column
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id_goal")
    private Goal goal;

}
