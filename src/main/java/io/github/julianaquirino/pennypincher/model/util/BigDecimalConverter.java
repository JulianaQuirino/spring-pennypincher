package io.github.julianaquirino.pennypincher.model.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BigDecimalConverter {

    // 1.000,00 para 1000.00
    public BigDecimal converter(String value) {
        if (value == null) {
            return null;
        }

        value = value.replace(".", "").replace(",", ".");
        return new BigDecimal(value);
    }
}
