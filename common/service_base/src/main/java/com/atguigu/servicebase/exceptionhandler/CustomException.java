package com.atguigu.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 * @author lu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomException extends RuntimeException{
    //状态码
    private Integer code;
    //异常信息
    private String msg;

}
