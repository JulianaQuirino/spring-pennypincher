package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class GoalDTO {

    private Integer id;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String name;

    @NotNull(message = "{campo.valor.obrigatorio}")
    private String value;

    @NotEmpty(message = "{campo.usuario.obrigatorio}")
    private String usernameAppUser;

    public GoalDTO(Integer id, String name, BigDecimal value, String usernameAppUser) {
        this.id = id;
        this.name = name;
        this.value = value.toString();
        this.usernameAppUser = usernameAppUser;
    }

    public GoalDTO(Integer id, String name, String value, String usernameAppUser) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.usernameAppUser = usernameAppUser;
    }
}
