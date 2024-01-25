package com.kodilla.onlinecurrencyexchangebackend.nbp.tables;

public enum NBPTableType {
    A("A", "Table A exchange rates are updated on each business day between 11:45 and 12:15."),
    C("C", "Table C exchange rates are updated on each business day between 7:45 and 8:15.");

    private final String code;
    private final String updateTime;

    NBPTableType(String code, String updateTime) {
        this.code = code;
        this.updateTime = updateTime;
    }

    public String getCode() {
        return code;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
