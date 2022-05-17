package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ChartSubcategoriesDTO {
    private String subcategoryName;

    private Double total;

    public ChartSubcategoriesDTO(String subcategoryName, BigDecimal total){
        this.subcategoryName = subcategoryName;
        this.total = total.doubleValue();
    }

}
