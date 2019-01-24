package cn.izis.util.rx;

public class RxEvent {
//    public static final int tab_match_info_selected = 10000;
//    public static final int tab_match_users_selected = 10001;
    public static final int update_statusbar_text = 1000;
    public static final int update_statusbar_progress = 1001;
    /**
     * 编辑赛事
     */
    public static final int editMatch = 1;
    /**
     * 刷新赛事信息
     */
    public static final int refreshMatchInfo = 2;
    /**
     * 刷新用户列表
     */
    public static final int refreshMatchUsers = 3;
    /**
     * 刷新编排页面
     */
    public static final int refreshMatchArrange = 4;
    /**
     * 刷新历史赛事
     */
    public static final int refreshMatchHistory = 5;
//    public static final int refreshHelpCenter = 6;
    /**
     * 删除参赛用户
     */
    public static final int del_user = 7;
    /**
     * 删除赛事
     */
    public static final int del_match = 8;
    /**
     * 打印参赛名单
     */
    public static final int print_users = 9;


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
