package com.ulwx.tool.reflect;

import java.io.Serializable;

@FunctionalInterface
public interface CGetFun<T, R> extends Serializable {
    R apply(T t);
}