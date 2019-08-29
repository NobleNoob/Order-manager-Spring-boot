package com.mgs.snake.service.impl;

import com.mgs.snake.dao.OrderDetail;
import com.mgs.snake.dto.OrderDto;
import com.mgs.snake.enums.OrderStatusEnum;
import com.mgs.snake.enums.PaymentStatus;
import com.mgs.snake.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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

    private final String ORDER_ID = "1567080375573674034";

    @Test
    public void create() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(KeyUtil.genUniqueKey());
        orderDto.setBuyerName("hacker");
        orderDto.setBuyerAddress("Sample address");
        orderDto.setBuyerPhone("1212121121");
        orderDto.setBuyerOpenid(BUYER_OPEN_ID);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail detail = new OrderDetail();
        detail.setProductId("50002");
        detail.setProductQuantity(2);
        orderDetailList.add(detail);

        orderDto.setOrderDetails(orderDetailList);
        OrderDto result = orderService.create(orderDto);
        log.info("创建订单 result {}", result);
    }

    @Test
    public void findOne() throws Exception {
        OrderDto result = orderService.findOne(ORDER_ID);
        log.info("result {}", result);
        Assert.assertEquals(ORDER_ID,result.getOrderId());

    }

    @Test
    public void findList() {
        PageRequest request = new PageRequest(0,2);
        Page<OrderDto> orderDtoPage = orderService.findList(BUYER_OPEN_ID,request);
        Assert.assertNotEquals(0,orderDtoPage.getTotalElements());

    }

    @Test
    public void cancel() throws Exception {
        OrderDto orderDto = orderService.findOne(ORDER_ID);
        OrderDto result = orderService.cancel(orderDto);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDto orderDto = orderService.findOne(ORDER_ID);
        OrderDto result = orderService.finish(orderDto);
        Assert.assertEquals(OrderStatusEnum.FINISH.getCode(),result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDto orderDto = orderService.findOne(ORDER_ID);
        OrderDto result = orderService.paid(orderDto);
        Assert.assertEquals(PaymentStatus.FINISH.getCode(),result.getPayStatus());
    }
}