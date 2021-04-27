package hello.constants;

/**
 * Created by linrx1 on 2021/3/2.
 */
public enum ErrorEnum {
    success(1008610, "操作成功"),
    error1(1008611, "操作xxx失败"),
    error2(1008612, "操作xxx失败"),
    error3(1008613, "操作xxx失败"),
    error4(1008614, "操作xxx失败"),

    ;


    private int code;
    private String message;

    ErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
