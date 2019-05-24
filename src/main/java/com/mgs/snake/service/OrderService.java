package com.mgs.snake.service;

import com.mgs.snake.dataobject.OrderMaster;
import com.mgs.snake.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    //Create Order list
    OrderDto create(OrderDto orderDto);

    //Find list by OrderId
    OrderDto findOne(String orderId);

    //Search Order List
    Page<OrderDto> findList(String buyerOpenid, Pageable pageable);

    //Cancel Order
    OrderDto cancel(OrderDto orderDto);

    //Finish Order
    OrderDto finish(OrderDto orderDto);

    //Pay Order
    OrderDto paid(OrderDto orderDto);


}
