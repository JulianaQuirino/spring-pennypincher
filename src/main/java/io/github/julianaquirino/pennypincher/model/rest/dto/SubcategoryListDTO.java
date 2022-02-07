package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubcategoryListDTO {

    private Integer id;

    private String name;

    private String categoryName;

}
