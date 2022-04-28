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

    public String getMonthDescription(){
        switch(getMonth()){
            case 1:
                return "Janeiro";
            case 2:
                return "Fevereiro";
            case 3:
                return "Março";
            case 4:
                return "Abril";
            case 5:
                return "Maio";
            case 6:
                return "Junho";
            case 7:
                return "Julho";
            case 8:
                return "Agosto";
            case 9:
                return "Setembro";
            case 10:
                return "Outubro";
            case 11:
                return "Novembro";
            case 12:
                return "Dezembro";
            default:
                return "";
        }

    }
}

