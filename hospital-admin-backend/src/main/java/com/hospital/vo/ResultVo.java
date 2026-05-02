package com.hospital.vo;

import lombok.Data;

@Data
public class ResultVo<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ResultVo<T> success() {
        return success(null);
    }

    public static <T> ResultVo<T> success(T data) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> ResultVo<T> error(String message) {
        return error(500, message);
    }

    public static <T> ResultVo<T> error(int code, String message) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> ResultVo<T> unauthorized() {
        return error(401, "未登录或Token已过期");
    }

    public static <T> ResultVo<T> forbidden() {
        return error(403, "无权限访问");
    }
}
