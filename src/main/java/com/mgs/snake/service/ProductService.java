package com.mgs.snake.service;

import com.mgs.snake.dao.ProductInfo;
import com.mgs.snake.dto.CartDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductInfo findOne(String productId);

    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    void increaseStock(List<CartDto> cartDtos);

    void decreaseStock(List<CartDto> cartDtos);

}
