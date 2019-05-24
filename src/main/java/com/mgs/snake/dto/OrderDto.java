package com.mgs.snake.dto;

import com.mgs.snake.dataobject.OrderDetail;
import com.mgs.snake.enums.OrderStatus;
import com.mgs.snake.enums.PaymentStatus;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
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
    private Integer orderStatus = OrderStatus.NEW.getCode();

    @Column(name = "pay_status")
    private Integer payStatus = PaymentStatus.UNPAY.getCode();

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Transient
    private List<OrderDetail> orderDetails;

}
