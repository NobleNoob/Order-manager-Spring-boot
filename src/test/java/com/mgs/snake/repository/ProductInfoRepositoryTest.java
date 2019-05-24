package com.mgs.snake.repository;

import com.mgs.snake.dataobject.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository repository;

    @Test
    public void saveTest(){
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("463721");
        productInfo.setProductName("PS4");
        productInfo.setCategoryType(10);
        productInfo.setProductDescription("Play Station 4");
        productInfo.setProductPrice(new BigDecimal(2000.0));
        productInfo.setProductStock(100);
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(2);
        productInfo.setProductIcon("http://xxxx.jpg");

        ProductInfo result= repository.save(productInfo);
        Assert.assertNotNull(result);
    }

    @Test
    public void findByProductStatus() throws Exception {
        List<ProductInfo> productInfos = repository.findByProductStatus(0);
        Assert.assertNotEquals(0,productInfos.size());
    }

}