package com.ulwx.type;

public class TByte implements TType {

    private Byte value;


    public TByte() {

    }

    public TByte(Byte v) {
        this.value = v;
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }

    public String toString() {
        return value + "";
    }

    @Override
    public Class wrappedClass() {
        return Byte.class;
    }
}
