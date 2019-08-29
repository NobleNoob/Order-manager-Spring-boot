package com.mgs.snake.utils.converter;

import com.mgs.snake.dao.OrderMaster;
import com.mgs.snake.dto.OrderDto;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMasterToOrderDto {

    public static OrderDto convert(OrderMaster orderMaster){
        OrderDto orderDto = new OrderDto();

        BeanUtils.copyProperties(orderMaster,orderDto);
        return orderDto;
    }

//      return orderMasterList.stream().map(e -> convert(e)).collect(Collectors.toList());

        public static List<OrderDto> convert(List<OrderMaster> orderMasterList) {
            List<OrderDto> orderDtoList = new ArrayList<>();
            for(OrderMaster orderMaster : orderMasterList) {
                orderDtoList.add(convert(orderMaster));
            }
            return orderDtoList;
        }

    }

