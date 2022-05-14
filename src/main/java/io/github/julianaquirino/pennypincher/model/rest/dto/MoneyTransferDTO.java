package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class MoneyTransferDTO {

    private Integer id;

    @NotNull(message = "{campo.data.obrigatorio}")
    private String date;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String description;

    @NotNull(message = "{campo.conta.debito.obrigatorio}")
    private Integer debitAccountId;

    @NotNull(message = "{campo.conta.credito.obrigatorio}")
    private Integer creditAccountId;

    @NotNull(message = "{campo.valor.obrigatorio}")
    private String value;

    private Integer goalId;

    @NotNull(message = "{campo.usuario.obrigatorio}")
    private String usernameAppUser;

}

