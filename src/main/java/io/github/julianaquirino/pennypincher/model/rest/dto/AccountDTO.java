package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AccountDTO {

    private Integer id;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String name;

    private String description;

    @NotEmpty(message = "{campo.usuario.obrigatorio}")
    private String usernameAppUser;

    public AccountDTO(Integer id, String name, String description, String usernameAppUser) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.usernameAppUser = usernameAppUser;
    }
}
