package io.github.julianaquirino.pennypincher.model.rest.dto;

import io.github.julianaquirino.pennypincher.model.util.CategoryType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class RecordsReportDTO {

    private String date;

    private String categoryName;

    private String subcategoryName;

    private String categoryType;

    private String description;

    private String value;

    private String accountName;

    private String projectName;

    public RecordsReportDTO(LocalDate date, String categoryName, String subcategoryName,
                            CategoryType categoryType, String description, BigDecimal value,
                            String accountName, String projectName) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);
        this.date = formattedDate;
        this.categoryName = categoryName;
        this.subcategoryName = subcategoryName;
        this.categoryType = categoryType.toString();
        this.description = description;
        String strValue = value.toString();
        strValue = strValue.replace(".", ",");
        this.value = strValue;
        this.accountName = accountName;
        this.projectName = projectName;
    }
}
