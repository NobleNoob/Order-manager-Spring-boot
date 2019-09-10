package com.mgs.snake.controller;


import com.mgs.snake.VO.ResultVO;
import com.mgs.snake.dao.OrderDetail;
import com.mgs.snake.dao.OrderMaster;
import com.mgs.snake.dto.OrderDto;
import com.mgs.snake.enums.ResultEnum;
import com.mgs.snake.exceptions.SellException;
import com.mgs.snake.form.OrderForm;
import com.mgs.snake.service.BuyerService;
import com.mgs.snake.service.OrderService;
import com.mgs.snake.utils.ResultVOUtil;
import com.mgs.snake.utils.converter.OrderFormToOrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            log.error("Item argument is incorrect");
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());

        }
        OrderDto orderDto = OrderFormToOrderDto.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDto.getOrderDetails())){
            log.error("orderDetail is empty");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDto orderResult = orderService.create(orderDto);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",orderResult.getOrderId());
        return ResultVOUtil.success(map);
    }

    @GetMapping("/list")
    public ResultVO<List<OrderDto>> list(
            @RequestParam(name = "openId") String openId,
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size) {
        if (StringUtils.isEmpty(openId)) {
            log.error("openId is empty");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        PageRequest pageRequest = new PageRequest(page,size);
        Page<OrderDto> orderDtos = orderService.findList(openId,pageRequest);
        return ResultVOUtil.success();
    }

    @GetMapping("/detail")
    public ResultVO<OrderDto> detail(
            @RequestParam(name = "openId") String openId,
            @RequestParam(name = "orderId") String orderId) {
        OrderDto orderDto = buyerService.findOrderOne(openId,orderId);
        if(!orderDto.getBuyerOpenid().equals(openId)) {

        }
        return ResultVOUtil.success(orderDto);
        }

    @PostMapping("/cancel")
    public ResultVO<OrderDto> cancel(
            @RequestParam("openId") String openId,
            @RequestParam("orderId") String orderId)
    {
        buyerService.cancelOrder(openId,orderId);
        return ResultVOUtil.success();

    }
}
