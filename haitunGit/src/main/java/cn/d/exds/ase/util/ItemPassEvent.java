package cn.d.exds.ase.util;

/**
 * Created by schwager on 2016/8/24.
 */
public class ItemPassEvent {

    private int passRessult;
    private int pos;
    private String desc;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    public ItemPassEvent(int passRessult, int pos, String desc) {
        this.passRessult = passRessult;
        this.pos = pos;
        this.desc = desc;
    }

    public ItemPassEvent(int passRessult, int pos, String desc, boolean flag) {
        this.passRessult = passRessult;
        this.pos = pos;
        this.desc = desc;
        this.flag = flag;
    }

    public int getPassRessult() {
        return passRessult;
    }

    public void setPassRessult(int passRessult) {
        this.passRessult = passRessult;
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


}
