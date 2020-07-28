package com.demo.workshop.intern.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author MIAOOY2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> implements Serializable {

    private static final long serialVersionUID = -9145063821855002279L;

    public static final int SUCCESS = 1;

    public static final int FAIL = 0;

    private String msg;

    private int count;

    private int code;

    private T data;


    public static ResultDto fail(String msg){
        return new ResultDto<>(msg , 1, FAIL, null);
    }

    public static ResultDto success(){
        return new ResultDto<>("success", 0, SUCCESS, null);
    }

    public static <T> ResultDto success(T data){
        return new ResultDto<>("success", 0, SUCCESS, data);
    }

    public static <T>ResultDto fail(T data){
        return new ResultDto<>("fail", 0, FAIL, data);
    }

    public static <T> ResultDto success(String msg,T data){
        return new ResultDto<>(msg, 0, SUCCESS, data);
    }

    public static <T> ResultDto success(int count,T data){
        return new ResultDto<>("success", count, SUCCESS, data);
    }

    public static <T> ResultDto success(String msg, int count, T data){
        return new ResultDto<>(msg, count, SUCCESS, data);
    }
}
