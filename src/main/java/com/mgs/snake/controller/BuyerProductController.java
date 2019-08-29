package com.mgs.snake.controller;


import com.mgs.snake.VO.ProductInfoVO;
import com.mgs.snake.VO.ProductVO;
import com.mgs.snake.VO.ResultVO;
import com.mgs.snake.dao.ProductCategory;
import com.mgs.snake.dao.ProductInfo;
import com.mgs.snake.service.CategoryService;
import com.mgs.snake.service.ProductService;
import com.mgs.snake.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ResultVO list() {
        //search all products from database
        List<ProductInfo> productInfoList = productService.findUpAll();
        //insert typeId from productInfo
        List<Integer> categoryTypeList = new ArrayList<>();
        for(ProductInfo productInfo : productInfoList) {
            categoryTypeList.add(productInfo.getCategoryType());
        }
        List<ProductVO> productVOList = new ArrayList<>();
        //catch Category list
        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        for(ProductCategory productCategory: productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo: productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    productInfoVO.setProductId(productInfo.getProductId());
                    productInfoVO.setProductDescription(productInfo.getProductDescription());
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }

            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);

    }
}
