package com.mgs.snake.service.impl;

import com.mgs.snake.dto.OrderDto;
import com.mgs.snake.enums.ResultEnum;
import com.mgs.snake.exceptions.SellException;
import com.mgs.snake.service.BuyerService;
import com.mgs.snake.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyserServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDto findOrderOne(String openId, String orderId) {
        return checkOrderOwner(openId,orderId);
    }

    @Override
    public OrderDto cancelOrder(String openId, String OrderId) {
        OrderDto orderDto = checkOrderOwner(openId,OrderId);
        if (orderDto == null)
        {
            log.error("Order is empty cancel is failed, orderid={},orderDto={}",openId,orderDto);
            throw new SellException(ResultEnum.ORDER_IS_NOT_EXISTENT);
        }
        return orderService.cancel(orderDto);
    }

    private OrderDto checkOrderOwner(String openId, String orderId) {
        OrderDto orderDto = orderService.findOne(orderId);
        if (orderDto == null ) {
            return null;
        }
        if (!orderDto.getBuyerOpenid().equalsIgnoreCase(openId)){
            log.error("OpenId is not equal, openid={},orderDTO={}",openId,orderDto);
            throw new SellException(ResultEnum.ORDER_NOT_EQUAL);
        }
        return orderDto;
    }
}
