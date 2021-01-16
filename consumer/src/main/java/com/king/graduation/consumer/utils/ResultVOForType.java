package com.king.graduation.consumer.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @Author king
 * @date 2020/11/30
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Getter
@Setter
public class  ResultVOForType implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public ResultVOForType getSuccessResultVO(String msg) {
        return getSuccessWithNoDate(msg);
    }

    public <T> ResultVOForType getSuccessResultVO(String msg, T data) {
        return getSuccessWithDate(msg, data);
    }

    public ResultVOForType getFailureResultVO(String msg) {
        return getFailureWithNoDate(msg);
    }

    public <T> ResultVOForType getFailureResultVO(String msg, T data) {
        return getFailureWithDate(msg, data);
    }

    public ResultVOForType() {
    }

    public ResultVOForType(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVOForType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResultVOForType getSuccessWithNoDate(String msg) {
        return new ResultVOForType(ResultVO.getSuccessStatus(), msg);
    }

    private <T> ResultVOForType getSuccessWithDate(String msg, T data) {
        return new ResultVOForType(ResultVO.getSuccessStatus(), msg, data);
    }

    private ResultVOForType getFailureWithNoDate(String msg) {
        return new ResultVOForType(ResultVO.getFailureStatus(), msg);
    }

    private <T> ResultVOForType getFailureWithDate(String msg, T data) {
        return new ResultVOForType(ResultVO.getFailureStatus(), msg, data);
    }
}
