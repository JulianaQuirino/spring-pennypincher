package io.github.julianaquirino.pennypincher.model.util;

public enum CategoryType {

    D ("Debit"),
    C ("Credit");

    private final String categoryType;

    CategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryType() {
        return categoryType;
    }

}
