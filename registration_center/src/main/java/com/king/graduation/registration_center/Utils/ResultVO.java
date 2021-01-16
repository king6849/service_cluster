package com.king.graduation.registration_center.Utils;

import java.io.Serializable;

/**
 * @Author king
 * @date 2020/11/19
 */

public class ResultVO implements Serializable {
    private static final int successStatus = 100;
    private static final int failureStatus = 200;

    private int code;
    private String msg;
    private Object data;

    public ResultVO() {
    }

    public ResultVO(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResultVO getSuccessResultVO(String msg) {
        return getSuccessNoData(msg);
    }

    public static ResultVO getSuccessResultVO(String msg, Object data) {
        return getSuccessWithData(msg, data);
    }

    public static ResultVO getFailureResultVO(String msg) {
        return getFailureNoData(msg);
    }

    public ResultVO getFailureResultVO(String msg, Object data) {
        return getFailureWithData(msg, data);
    }


    private static ResultVO getSuccessNoData(String msg) {
        return new ResultVO(successStatus, msg);
    }

    private static ResultVO getSuccessWithData(String msg, Object data) {
        return new ResultVO(successStatus, msg, data);
    }

    private static ResultVO getFailureNoData(String msg) {
        return new ResultVO(failureStatus, msg);
    }

    private static ResultVO getFailureWithData(String msg, Object data) {
        return new ResultVO(failureStatus, msg, data);
    }


    public static int getSuccessStatus() {
        return successStatus;
    }

    public static int getFailureStatus() {
        return failureStatus;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
