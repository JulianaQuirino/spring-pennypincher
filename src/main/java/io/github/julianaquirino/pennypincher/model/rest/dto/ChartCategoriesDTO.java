package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ChartCategoriesDTO {

    private int month;

    private int year;

    private String categoryName;

    private Double total;

    public ChartCategoriesDTO(int month, int year, String categoryName, BigDecimal total){
        this.month = month;
        this.year = year;
        this.categoryName = categoryName;
        this.total = total.doubleValue();
    }

}
