package com.mgs.snake.service.impl;

import com.mgs.snake.dao.OrderDetail;
import com.mgs.snake.dao.OrderMaster;
import com.mgs.snake.dao.ProductInfo;
import com.mgs.snake.dto.CartDto;
import com.mgs.snake.dto.OrderDto;
import com.mgs.snake.enums.OrderStatusEnum;
import com.mgs.snake.enums.PaymentStatus;
import com.mgs.snake.enums.ResultEnum;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Page<OrderDto> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        List<OrderDto> orderDtos = OrderMasterToOrderDto.convert(orderMasterPage.getContent());
        return new PageImpl<OrderDto>(orderDtos,pageable,orderMasterPage.getTotalElements());


    }

    @Override
    public OrderDto findOne(String orderId) {
        OrderMaster orderMaster = orderMasterRepository.findOne(orderId);
        if (orderMaster == null){
            throw new SellException(ResultEnum.ORDER_IS_NOT_EXISTENT);
        }

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDER_DETAIL_IS_NOT_EXISTENT);
        }

        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(orderMaster,orderDto);
        orderDto.setOrderDetails(orderDetailList);
        return orderDto;
    }


    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        String orderId = KeyUtil.genUniqueKey();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
//        List<CartDto> cartDtoList  = new ArrayList<>();

        //Search product_number and product_price
        for (OrderDetail orderDetail: orderDto.getOrderDetails()){
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXISTENT);
            }

            /*
            * order amount price
            * orderAmount = price * product_number + 0
            */
            orderAmount = productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()).add(orderAmount));
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);
            //insert detail to orderDetail
            orderDetailRepository.save(orderDetail);
//            CartDto cartDto = new CartDto(orderDetail.getProductId(),orderDetail.getProductQuantity());
//            cartDtoList.add(cartDto);
        }

        //insert orderMaster and orderDetail into database
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderId(orderId);
        BeanUtils.copyProperties(orderDto,orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PaymentStatus.UNPAY.getCode());
        orderMasterRepository.save(orderMaster);

        //decrease stock from store
        List<CartDto> cartDTOList = orderDto.getOrderDetails().stream().map(e ->
                new CartDto(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());

        productService.decreaseStock(cartDTOList);


        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto cancel(OrderDto orderDto) {
        OrderMaster orderMaster = new OrderMaster();

        //find Order Status
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("Order status is not correct,{},{}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //modify Order Status
        orderDto.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster orderUpdate = orderMasterRepository.save(orderMaster);
        if (orderUpdate == null) {
            log.error("Order cancle is failed, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        //return Store
        if(CollectionUtils.isEmpty(orderDto.getOrderDetails())) {
            log.error("Return product to store,orderDto{}",orderDto);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);

        }
//            List<CartDto> cartDtos = orderDto.getOrderDetails()
//            .stream()
//            .map(e -> new CartDto(e.getProductId(),e.getProductQuantity())).collect(Collectors.toList());
            List<CartDto> cartDtos = new ArrayList<>();
            for (OrderDetail orderDetail: orderDto.getOrderDetails()) {
                CartDto cartDto = new  CartDto(orderDetail.getProductId(), orderDetail.getProductQuantity());
                cartDtos.add(cartDto);
            }
            productService.increaseStock(cartDtos);

        //refund if has paid
        if (orderDto.getOrderStatus().equals(PaymentStatus.FINISH.getCode())) {
            //TODO
        }
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto finish(OrderDto orderDto) {
        //define order status
        if (!orderDto.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("Order status is not correct orderId={},orderStatus={}",orderDto.getOrderId(),orderDto.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster orderUpdate = orderMasterRepository.save(orderMaster);
        if (orderUpdate == null) {
            log.error("Order has not finish, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        //adjust order status
        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto paid(OrderDto orderDto) {
        if (!orderDto.getPayStatus().equals(PaymentStatus.UNPAY.getCode())) {
            log.error("Order status is not correct orderId={},payStatus={}",orderDto.getOrderId(),orderDto.getPayStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        if (!orderDto.getPayStatus().equals(PaymentStatus.WAIT.getCode())){
            log.error("Order status is not correct order={}",orderDto);
            throw new SellException(ResultEnum.PAID_IS_FAILED);
        }
        OrderMaster orderMaster = new OrderMaster();
        orderDto.setPayStatus(PaymentStatus.FINISH.getCode());
        BeanUtils.copyProperties(orderDto,orderMaster);
        OrderMaster orderUpdate =orderMasterRepository.save(orderMaster);
        if (orderUpdate == null) {
            log.error("Order has not finish, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAILED);
        }
        return orderDto;
    }
}
