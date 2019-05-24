package com.mgs.snake.enums;


import lombok.Getter;

@Getter
public enum  OrderStatus {
    NEW(0,"NEW"),
    FINISH(1,"FINISH"),
    CANCEL(2,"CANCEl");

    private Integer code;

    private String message;

    OrderStatus(Integer code,String message) {
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
