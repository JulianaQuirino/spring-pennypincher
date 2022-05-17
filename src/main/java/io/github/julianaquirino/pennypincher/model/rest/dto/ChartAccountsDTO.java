package io.github.julianaquirino.pennypincher.model.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ChartAccountsDTO {

    private String accountName;

    private Double balance;

    public ChartAccountsDTO(String accountName, BigDecimal balance){
        this.accountName = accountName;
        this.balance = balance.doubleValue();
    }

    public ChartAccountsDTO(String accountName, BigDecimal credit, BigDecimal debit){
        this.accountName = accountName;
        this.balance = credit.doubleValue() - debit.doubleValue();
    }



}
