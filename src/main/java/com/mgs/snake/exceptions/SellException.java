package com.mgs.snake.exceptions;

import com.mgs.snake.enums.Result;

public class SellException extends RuntimeException {
    private Integer code;

    public SellException(Result result) {
        super(result.getMessage());

        this.code = result.getCode();
    }
}
