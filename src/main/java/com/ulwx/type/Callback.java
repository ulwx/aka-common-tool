package com.ulwx.type;

public interface Callback<T1, T2> {

    public T2 call(T1 obj);
}
