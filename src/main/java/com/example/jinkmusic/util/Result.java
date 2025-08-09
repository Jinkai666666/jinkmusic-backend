package com.example.jinkmusic.util;

/**
 * 通用返回结果类，用于封装 API 接口统一格式
 * @param <T> data字段的数据类型，可以是字符串、对象、列表等
 */
public class Result<T> {
    private int code;     // 状态码， 200 表示成功，400 表示失败
    private String msg;   // 提示信息，如 “成功” / “用户名已存在”
    private T data;       // 返回的具体数据，token、用户信息等

    // 空构造函数，框架需要用
    public Result() {}

    // 构造函数
    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 返回成功结果（带自定义提示和数据）
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    // 返回失败结果（带错误提示）
    public static <T> Result<T> error(String msg) {
        return new Result<>(400, msg, null);
    }

    // Getter 和 Setter（用于序列化为 JSON）
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

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
