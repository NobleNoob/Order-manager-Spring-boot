package com.mgs.snake.service;

import com.mgs.snake.dto.OrderDto;

public interface BuyerService {

    OrderDto findOrderOne(String openId, String orderId);

    OrderDto cancelOrder(String openId, String orderId);

}
