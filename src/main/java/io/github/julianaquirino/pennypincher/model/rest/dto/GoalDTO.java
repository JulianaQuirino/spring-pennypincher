package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
}
