package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class LimitDTO {

    private Integer id;

    @NotNull(message = "{campo.valor.obrigatorio}")
    private String maxValue;

    @NotNull(message = "{campo.categoria.obrigatorio}")
    private Integer categoryId;

    @NotNull(message = "{campo.mes.obrigatorio}")
    private Integer month;

    @NotNull(message = "{campo.ano.obrigatorio}")
    private Integer year;
}
