package com.mgs.snake.dao;

import com.mgs.snake.enums.OrderStatusEnum;
import com.mgs.snake.enums.PaymentStatus;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@DynamicUpdate
@Table(name = "order_master")
@Data
public class OrderMaster {

    @Id
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

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;


}
