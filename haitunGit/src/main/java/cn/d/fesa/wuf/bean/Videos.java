package cn.d.fesa.wuf.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by BGFVG on 2017/2/8.
 */

public class Videos extends DataSupport {
    private int id;

    private String videoId;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }


}
