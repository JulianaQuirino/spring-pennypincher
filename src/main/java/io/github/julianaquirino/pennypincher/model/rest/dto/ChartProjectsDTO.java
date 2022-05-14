package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ChartProjectsDTO {

    private String monthYear;

    private Double total;

    public ChartProjectsDTO(int month, int year, BigDecimal total){
        this.monthYear = String.valueOf(month) + "/" + String.valueOf(year);
        this.total = total.doubleValue();
    }

}
