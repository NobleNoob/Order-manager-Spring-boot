package com.mgs.snake.service.impl;

import com.mgs.snake.dao.OrderDetail;
import com.mgs.snake.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceImplTest {


    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPEN_ID = "123456";

    private final String ORDER_ID = "1558604159315219093";

    @Test
    public void create() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(ORDER_ID);
        orderDto.setBuyerName("hacker");
        orderDto.setBuyerAddress("Sample address");
        orderDto.setBuyerPhone("50002");
        orderDto.setBuyerOpenid(BUYER_OPEN_ID);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail detail = new OrderDetail();
        detail.setProductId("12324");
        detail.setProductQuantity(1);
        orderDetailList.add(detail);

        orderDto.setOrderDetails(orderDetailList);
        OrderDto result = orderService.create(orderDto);
        log.info("创建订单 result {}", result);
    }

    @Test
    public void findOne() throws Exception {
        OrderDto result = orderService.findOne(ORDER_ID);
        log.info("result {}", result);

    }

    @Test
    public void findList() {
        PageRequest request = new PageRequest(0,2);
        Page<OrderDto> orderDtoPage = orderService.findList(BUYER_OPEN_ID,request);

    }

    @Test
    public void cancel() {
    }

    @Test
    public void finish() {
    }

    @Test
    public void paid() {
    }
}