package com.mgs.snake.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mgs.snake.dao.OrderDetail;
import com.mgs.snake.enums.OrderStatusEnum;
import com.mgs.snake.enums.PaymentStatus;
import com.mgs.snake.utils.serializer.Date2LongSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "buyer_name")
    private String buyerName;

    @Column(name = "buyer_phone")
    private String buyerPhone;

    @Column(name = "buyer_address")
    private String buyerAddress;

    @Column(name = "buyer_openid")
    private String buyerOpenid;

    @Column(name = "order_amount")
    private BigDecimal orderAmount;

    @Column(name = "order_status")
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    @Column(name = "pay_status")
    private Integer payStatus = PaymentStatus.UNPAY.getCode();

    @JsonSerialize(using = Date2LongSerializer.class)
    @Column(name = "create_time")
    private Date createTime;

    @JsonSerialize(using = Date2LongSerializer.class)
    @Column(name = "update_time")
    private Date updateTime;

    @Transient
    private List<OrderDetail> orderDetails = new ArrayList<>();

}
