package com.mgs.snake.dto;

import lombok.Data;

@Data
public class CartDto {

    private String productId;

    private Integer productQuality;

    public CartDto(String productId, Integer productQuality) {
        this.productId = productId;
        this.productQuality = productQuality;
    }
}
