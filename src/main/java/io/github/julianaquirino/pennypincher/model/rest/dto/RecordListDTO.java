package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecordListDTO {
    private Integer id;

    private String accountName;

    private String categoryName;

    private String categoryType;

    private String subcategoryName;

    private String value;

    private String description;

    private String projectName;

}
