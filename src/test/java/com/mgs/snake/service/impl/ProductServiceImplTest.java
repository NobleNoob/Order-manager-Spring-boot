package com.mgs.snake.service.impl;

import com.mgs.snake.dao.ProductInfo;
import com.mgs.snake.enums.ProductStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Test
    public void findOne() throws Exception {
       ProductInfo productInfo =  productService.findOne("463721");
       Assert.assertNotNull(productInfo);
    }

    @Test
    public void findUpAll() throws Exception {
        List<ProductInfo> productInfo = productService.findUpAll();
        Assert.assertNotEquals(0,productInfo);
    }

    @Test
    public void findAll() throws Exception {
        PageRequest request = new PageRequest(0,2);
        Page<ProductInfo> productInfo = productService.findAll(request);
        Assert.assertNotEquals(0,productInfo);
    }

    @Test
    public void save() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("50002");
        productInfo.setProductName("pantyhose");
        productInfo.setCategoryType(10);
        productInfo.setProductDescription("Sexy");
        productInfo.setProductPrice(new BigDecimal(15.0));
        productInfo.setProductStock(100);
        productInfo.setProductStatus(ProductStatus.UP.getCode());
        productInfo.setProductIcon("http://xxxx.jpg");

        ProductInfo result = productService.save(productInfo);
        Assert.assertNotNull(result);

    }

}