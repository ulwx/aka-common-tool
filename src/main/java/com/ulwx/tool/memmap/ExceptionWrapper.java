package com.ulwx.tool.memmap;

public class ExceptionWrapper {


    private Exception exception;

    public synchronized Exception getException() {
        return exception;
    }

    public synchronized void setException(Exception exception) {
        this.exception = exception;
    }
}
