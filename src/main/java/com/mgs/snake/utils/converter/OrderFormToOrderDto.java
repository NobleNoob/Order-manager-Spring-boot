package com.mgs.snake.utils.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgs.snake.dao.OrderDetail;
import com.mgs.snake.dto.OrderDto;
import com.mgs.snake.enums.ResultEnum;
import com.mgs.snake.exceptions.SellException;
import com.mgs.snake.form.OrderForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderFormToOrderDto {
    public static OrderDto convert(OrderForm orderForm){
        Gson gson = new Gson();

        OrderDto orderDto = new OrderDto();
        orderDto.setBuyerName(orderForm.getName());
        orderDto.setBuyerAddress(orderForm.getAddress());
        orderDto.setBuyerOpenid(orderForm.getOpenId());
        orderDto.setBuyerPhone(orderForm.getPhoneNumber());
        List<OrderDetail> orderDetailList = new ArrayList<>();
        try {
            orderDetailList  = gson.fromJson(orderForm.getItems(),
                    new TypeToken<List<OrderDetail>>(){}.getType());
        } catch (Exception e) {
            log.error("Error log ,String={}",orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        orderDto.setOrderDetails(orderDetailList);

        return orderDto;

    }

//      return orderMasterList.stream().map(e -> convert(e)).collect(Collectors.toList());

        public static List<OrderDto> convert(List<OrderForm> orderMasterList) {
            List<OrderDto> orderDtoList = new ArrayList<>();
            for(OrderForm orderForm : orderMasterList) {
                orderDtoList.add(convert(orderForm));
            }
            return orderDtoList;
        }

    }

