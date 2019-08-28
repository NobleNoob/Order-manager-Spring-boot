package com.mgs.snake.repository;

import com.mgs.snake.dao.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository repository;

    @Test
    public void saveTest() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("123456");
        orderDetail.setOrderId("1111");
        orderDetail.setProductIcon("http://xxxx.jpg");
        orderDetail.setProductId("123456");
        orderDetail.setProductName("Snake");
        orderDetail.setProductPrice(new BigDecimal(2.1));
        orderDetail.setProductQuantity(2);
        OrderDetail result = repository.save(orderDetail);

    }

    @Test
    public void findByOrderId() throws Exception {

    }

}