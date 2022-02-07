package io.github.julianaquirino.pennypincher.model.rest.dto;

import io.github.julianaquirino.pennypincher.model.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RecordDTO {

    private Integer id;

    @NotNull(message = "{campo.data.obrigatorio}")
    private Integer dailyRecordId;

    @NotNull(message = "{campo.categoria.obrigatorio}")
    private Integer categoryId;

    private String type;

    private Integer subcategoryId;

    @NotNull(message = "{campo.valor.obrigatorio}")
    private String value;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String description;

    @NotNull(message = "{campo.conta.obrigatorio}")
    private Integer accountId;

    private Integer projectId;

}
