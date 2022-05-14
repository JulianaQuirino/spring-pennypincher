package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class MoneyTransferListDTO {

    private Integer id;

    private String date;

    private String description;

    private String debitAccountName;

    private String creditAccountName;

    private String value;

    private String goalName;

    public MoneyTransferListDTO (int id, LocalDate moneyTransferDate, String description, String debitAccountName,
                                 String creditAccountName, BigDecimal value, String goalName){
        this.id = id;
        String pattern = "dd/MM/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = moneyTransferDate.format(formatter);
        this.date = formattedDate;
        this.description = description;
        this.debitAccountName = debitAccountName;
        this.creditAccountName = creditAccountName;
        this.value = value.toString();
        this.goalName = goalName;


    }
}
