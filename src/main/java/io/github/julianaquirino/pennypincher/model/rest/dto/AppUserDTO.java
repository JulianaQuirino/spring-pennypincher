package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class AppUserDTO {

    private Integer id;

    @NotEmpty(message = "{campo.usuario.obrigatorio}")
    private String username;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String name;

    @NotEmpty(message = "{campo.telefone.obrigatorio}")
    private String phone;

    @NotEmpty(message = "{campo.email.obrigatorio}")
    private String email;

    private boolean admin;

    private String password;

}
