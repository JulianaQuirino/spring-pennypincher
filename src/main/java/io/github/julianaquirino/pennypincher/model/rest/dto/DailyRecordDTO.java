package io.github.julianaquirino.pennypincher.model.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DailyRecordDTO {

    private Integer id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "{campo.data.obrigatorio}")
    private String date;

    private String debits;

    private String credits;

    @NotNull(message = "{campo.usuario.obrigatorio}")
    private String usernameAppUser;
}
