package com.xwkj.ygjces.common;

public class ResponseResult {
    private int code;
    private Object data;
    private String msg;
    private Boolean success;

    public static ResponseResult failure(){
        return failure("操作失败");
    }

    public static ResponseResult failure(String msg){
        return failure(ResponseResultEnum.FAILURE.getCode(), msg);
    }

    public static ResponseResult failure(int code, String msg){
        return failure(code, msg, Boolean.FALSE);
    }

    public static ResponseResult failure(int code, String msg, boolean success){
        return of(code, null, msg, success);
    }

    public static ResponseResult success(Object data){return success(ResponseResultEnum.SUCCESS.getCode(), data, "操作成功！");}

    public static ResponseResult success(){
        return success(ResponseResultEnum.SUCCESS.getCode(), null, "操作成功！");
    }

    public static ResponseResult success (int code, Object data, String msg){
        return of(code, data, msg);
    }

    public static ResponseResult of(int code, Object data, String msg){
        return of(code, data, msg, Boolean.TRUE);
    }

    public static ResponseResult of(int code, Object data, String msg, Boolean success){
        return new ResponseResult(code, data, msg, success);
    }

    public ResponseResult() {
    }

    public ResponseResult(int code, Object data, String msg, Boolean success) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
