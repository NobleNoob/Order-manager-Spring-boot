package com.mgs.snake.service.impl;

import com.mgs.snake.dataobject.OrderDetail;
import com.mgs.snake.dataobject.OrderMaster;
import com.mgs.snake.dataobject.ProductInfo;
import com.mgs.snake.dto.CartDto;
import com.mgs.snake.dto.OrderDto;
import com.mgs.snake.enums.OrderStatus;
import com.mgs.snake.enums.Result;
import com.mgs.snake.exceptions.SellException;
import com.mgs.snake.repository.OrderDetailRepository;
import com.mgs.snake.repository.OrderMasterRepository;
import com.mgs.snake.service.OrderService;
import com.mgs.snake.service.ProductService;
import com.mgs.snake.utils.KeyUtil;
import com.mgs.snake.utils.converter.OrderMasterToOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(0);
        List<CartDto> cartDtos  = new ArrayList<>();

        //Search product_number and product_price
        for (OrderDetail orderDetail: orderDto.getOrderDetails()){
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null){
                throw new SellException(Result.PRODUCT_NOT_EXISTENT);
            }

            //Count total price
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()).add(orderAmount));

            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);

            //insert detail to orderDetail
            orderDetailRepository.save(orderDetail);
            CartDto cartDto = new CartDto(orderDetail.getProductId(),orderDetail.getProductQuantity());
            cartDtos.add(cartDto);
        }

        //insert orderMaster into orderMaster table
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDto,orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMasterRepository.save(orderMaster);
        //decrease stock from store
        productService.decreaseStock(cartDtos);

        return orderDto;
    }

    @Override
    public OrderDto findOne(String orderId) {

        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null){
            throw new SellException(Result.ORDER_IS_NOT_EXISTENT);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(Result.ORDERDETAIL_IS_NOT_EXISTENT);
        }

        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        orderDto.setOrderDetails(orderDetailList);
        return orderDto;
    }

    @Override
    public Page<OrderDto> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> page = orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        List<OrderDto> orderDtos = OrderMasterToOrderDto.convert(page.getContent());
        return new PageImpl<OrderDto>(orderDtos,pageable,page.getTotalElements());


    }

    @Override
    public OrderDto cancel(OrderDto orderDto) {

        //find Order Status
        if (orderDto.getOrderStatus().equals(OrderStatus.NEW.getCode())){
            log.error("Order status is not correct");
        }
        //modify Order Status

        //return Store

        //refund

        return null;
    }

    @Override
    public OrderDto finish(OrderDto orderDto) {
        return null;
    }

    @Override
    public OrderDto paid(OrderDto orderDto) {
        return null;
    }
}
