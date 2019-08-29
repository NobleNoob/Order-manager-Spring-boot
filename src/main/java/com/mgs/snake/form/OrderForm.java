package com.mgs.snake.form;


import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class OrderForm {

    @NotEmpty(message = "Name is necessary")
    private String name;

    @NotEmpty(message = "Phone number is necessary")
    private String phoneNumber;

    @NotEmpty(message = "Address is necessary")
    private String address;

    @NotEmpty(message = "Openid is necessary")
    private String openId;

    @NotEmpty(message = "Iteams is necessary")
    private String items;


}
