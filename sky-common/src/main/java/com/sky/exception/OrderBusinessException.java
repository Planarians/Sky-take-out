package com.sky.exception;

import lombok.Getter;


@Getter
public class OrderBusinessException extends RuntimeException {


    private Integer code;

    public OrderBusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public OrderBusinessException(String message) {
        super(message);
        this.code = 500;
    }
}

