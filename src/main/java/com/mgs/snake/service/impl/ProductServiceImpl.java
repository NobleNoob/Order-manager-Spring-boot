package com.mgs.snake.service.impl;


import com.mgs.snake.dataobject.ProductInfo;
import com.mgs.snake.dto.CartDto;
import com.mgs.snake.enums.ProductStatus;
import com.mgs.snake.enums.Result;
import com.mgs.snake.exceptions.SellException;
import com.mgs.snake.repository.ProductInfoRepository;
import com.mgs.snake.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductInfoRepository repository;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatus.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    public void increaseStock(List<CartDto> cartDtos) {

    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDto> cartDtos) {

        for (CartDto cartDto: cartDtos) {
            ProductInfo productInfo = repository.findOne(cartDto.getProductId());
            if (productInfo == null){
                throw new SellException(Result.PRODUCT_NOT_EXISTENT);
            }
            //check the store from Product and stock
            Integer store = productInfo.getProductStock() - cartDto.getProductQuality();
            if (store < 0) {
                throw new SellException(Result.STORE_IS_NOT_ENOUGH);
            }
            productInfo.setProductStock(store);

            repository.save(productInfo);
        }

    }
}
