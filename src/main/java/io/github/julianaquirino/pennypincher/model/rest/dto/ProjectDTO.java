package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class ProjectDTO {

    private Integer id;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String name;

    private String description;

    @NotEmpty(message = "{campo.usuario.obrigatorio}")
    private String usernameAppUser;
}
