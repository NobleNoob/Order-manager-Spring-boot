package com.mgs.snake.enums;


import lombok.Getter;

@Getter
public enum PaymentStatus {
    UNPAY(0,"UNPAY"),
    FINISH(1,"PAIED");

    private Integer code;

    private String message;

    PaymentStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
