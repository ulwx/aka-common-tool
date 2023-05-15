package com.ulwx.tool.reflect;

import java.io.Serializable;

@FunctionalInterface
public interface GetFun<R> extends Serializable {
    R get();
}