package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ChartGoalsDTO {

    private String goalName;

    private Double goalValue;

    private Double accumulatedValue;

    public ChartGoalsDTO(String goalName, BigDecimal goalValue, BigDecimal accumulatedValue){
        this.goalName = goalName;
        this.goalValue = goalValue.doubleValue();
        this.accumulatedValue = accumulatedValue.doubleValue();
    }

}
