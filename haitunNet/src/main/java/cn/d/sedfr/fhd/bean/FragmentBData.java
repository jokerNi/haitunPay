package cn.d.sedfr.fhd.bean;

import java.util.List;

/**
 * Created by BGFVG on 2017/2/7.
 */

public class FragmentBData {
    private int page;
    private int totalPage;
    private List<ConBean> con;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ConBean> getCon() {
        return con;
    }

    public void setCon(List<ConBean> con) {
        this.con = con;
    }

    public static class ConBean {
        /**
         * nickName : 呵呵
         * sex : 1
         * headImg : http://static.toyihao.com/files/19190000/19191188.jpg?uptime=1473848105
         * userId : 14
         * videoNum : 5
         */

        private String nickName;
        private String sex;
        private String headImg;
        private String userId;
        private String videoNum;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getVideoNum() {
            return videoNum;
        }

        public void setVideoNum(String videoNum) {
            this.videoNum = videoNum;
        }
    }
}
