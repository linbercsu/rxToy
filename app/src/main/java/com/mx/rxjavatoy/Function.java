package com.mx.rxjavatoy;

public interface Function<T, R> {
    R apply(T t) throws Exception;
}
