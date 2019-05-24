package com.mgs.snake.enums;


import lombok.Getter;

@Getter
public enum Result {

    PRODUCT_NOT_EXISTENT(10,"Product not Existent"),
    STORE_IS_NOT_ENOUGH(20,"Store not Enough"),
    ORDER_IS_NOT_EXISTENT(30,"Order is not Existent"),
    ORDERDETAIL_IS_NOT_EXISTENT(40, "No Order Detail");



    Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;



}
