package io.github.julianaquirino.pennypincher.model.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "goal", schema = "public")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    @Column(nullable = false)
    private String name;

    @Column
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id_app_user")
    private AppUser appUser;

    @OneToMany(targetEntity=MoneyTransfer.class, mappedBy="goal", fetch = FetchType.LAZY)
    private List<MoneyTransfer> moneyTransfers = new ArrayList<>();

}
