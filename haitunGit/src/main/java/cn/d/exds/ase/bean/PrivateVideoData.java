package cn.d.exds.ase.bean;

import java.util.List;

/**
 * Created by BGFVG on 2017/1/23.
 */

public class PrivateVideoData {


    private int page;
    private int totalPage;
    private List<ConBean> con;
    private List<String> users;
    private List<String> videos;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

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
        public int getVideoNum() {
            return videoNum;
        }

        public void setVideoNum(int videoNum) {
            this.videoNum = videoNum;
        }

        private int videoNum;
        private String userId;
        private String nickName;
        private String sex;
        private String headImg;
        private String videoChatSj;
        private String imgAddress;
        private String videoPlayAddress;
        private String videoId;
        private String shareTItle;
        private String riqi;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

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

        public String getVideoChatSj() {
            return videoChatSj;
        }

        public void setVideoChatSj(String videoChatSj) {
            this.videoChatSj = videoChatSj;
        }

        public String getImgAddress() {
            return imgAddress;
        }

        public void setImgAddress(String imgAddress) {
            this.imgAddress = imgAddress;
        }

        public String getVideoPlayAddress() {
            return videoPlayAddress;
        }

        public void setVideoPlayAddress(String videoPlayAddress) {
            this.videoPlayAddress = videoPlayAddress;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getShareTItle() {
            return shareTItle;
        }

        public void setShareTItle(String shareTItle) {
            this.shareTItle = shareTItle;
        }

        public String getRiqi() {
            return riqi;
        }

        public void setRiqi(String riqi) {
            this.riqi = riqi;
        }
    }
}
