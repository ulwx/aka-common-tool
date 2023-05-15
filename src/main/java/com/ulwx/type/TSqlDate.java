package com.ulwx.type;

import java.sql.Date;

public class TSqlDate implements TType {
    private Date value;

    public TSqlDate() {

    }

    public TSqlDate(Date value) {
        this.value = value;
    }

    @Override
    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public String toString() {
        return value + "";
    }

    @Override
    public Class wrappedClass() {
        return Date.class;
    }
}
