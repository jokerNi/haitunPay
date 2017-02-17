package cn.d.sedfr.fhd.util;

/**
 * Created by schwager on 2016/8/24.
 */
public class PassEvent {

    private int passRessult;
    private int pos;
    private String desc;
    private boolean flag;
    private String userId;
    private int pagePos;

    public PassEvent(int passRessult, int pos, String desc, boolean flag, int pagePos) {
        this.passRessult = passRessult;
        this.pos = pos;
        this.desc = desc;
        this.flag = flag;
        this.pagePos = pagePos;
    }


    public int getPagePos() {

        return pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PassEvent(int passResult) {
        this.passRessult = passResult;
    }

    public PassEvent(int passRessult, int pos, String desc, boolean flag) {
        this.passRessult = passRessult;
        this.pos = pos;
        this.desc = desc;
        this.flag = flag;
    }

    public PassEvent(int passRessult, int pos, String desc, boolean flag, String userId) {
        this.passRessult = passRessult;
        this.pos = pos;
        this.desc = desc;
        this.flag = flag;
        this.userId = userId;
    }

    public PassEvent(int passRessult, int pos, String desc, boolean flag, String userId, int pagePos) {
        this.passRessult = passRessult;
        this.pos = pos;
        this.desc = desc;
        this.flag = flag;
        this.userId = userId;
        this.pagePos = pagePos;
    }

    public int getPassRessult() {
        return passRessult;
    }

    public void setPassRessult(int passRessult) {
        this.passRessult = passRessult;
    }
}
