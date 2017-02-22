package cn.d.sedfr.fhd.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by BGFVG on 2017/2/8.
 */

public class Users extends DataSupport {
    private int id;

    private String userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
