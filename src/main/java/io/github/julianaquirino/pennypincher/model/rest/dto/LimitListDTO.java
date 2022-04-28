package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LimitListDTO {
    private Integer id;

    private String categoryName;

    private String maxValue;

    private String month;

    private Integer year;

}
