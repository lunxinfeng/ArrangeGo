package cn.izis.util.rx;

public class RxEvent {
    /**
     * 编辑赛事
     */
    public static final int editMatch = 1;
    /**
     * 刷新赛事信息
     */
    public static final int refreshMatchInfo = 2;


    private int code;
    private Object object;

    public RxEvent(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public RxEvent(int code) {
        this.code = code;
        this.object = 0;
    }

    public RxEvent() {
    }

    public int getCode() {
        return code;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return "code:" + code + "\tobject:" + object;
    }
}
