package com.ulwx.type;

public class TLong implements TType {
    private Long value;

    @Override
    public Class wrappedClass() {
        return Long.class;
    }

    public TLong() {

    }

    public TLong(Long val) {
        this.value = val;
    }

    @Override
    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String toString() {
        return value + "";
    }
}
