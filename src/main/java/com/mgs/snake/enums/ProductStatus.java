package com.mgs.snake.enums;



public enum ProductStatus {

    UP(0,"Up"),DOWN(1,"Down");

    private Integer code;

    private String message;

    ProductStatus(Integer code,String message) {
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
