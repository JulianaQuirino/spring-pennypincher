package io.github.julianaquirino.pennypincher.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "limit", schema = "public")
public class Limit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @Column(nullable = false)
    private BigDecimal maxValue;

    @Column
    @NotNull(message = "{campo.mes.obrigatorio}")
    private Integer month;

    @Column
    @NotNull(message = "{campo.ano.obrigatorio}")
    private Integer year;

}

