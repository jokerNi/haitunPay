package cn.d.sedfr.fhd.bean;

import java.util.List;

/**
 * Created by BGFVG on 2017/1/23.
 */

public class PrivateVideoData2 {

    /**
     * page : 1
     * totalPage : 6
     * con : [{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170122/19193134_172446.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170122/19193134_172446.mp4","videoId":"12346","shareTItle":"来吧宝贝","videoNum":52,"riqi":"更新于1天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170122/19193134_135516.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170122/19193134_135516.mp4","videoId":"12347","shareTItle":"丝袜的*哦","videoNum":52,"riqi":"更新于1天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170122/19193134_134640.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170122/19193134_134640.mp4","videoId":"12407","shareTItle":"我的精彩开始了","videoNum":52,"riqi":"更新于1天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170121/19193134_152445.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170121/19193134_152445.mp4","videoId":"13430","shareTItle":"最后一个不要错过了","videoNum":52,"riqi":"更新于2天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170121/19193134_143126.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170121/19193134_143126.mp4","videoId":"13431","shareTItle":"受不了快来帮帮我，好想要啊","videoNum":52,"riqi":"更新于2天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170121/19193134_140039.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170121/19193134_140039.mp4","videoId":"12408","shareTItle":"精彩开始了","videoNum":52,"riqi":"更新于2天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170120/19193134_141529.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170120/19193134_141529.mp4","videoId":"2503","shareTItle":"来吧宝贝给个精彩","videoNum":52,"riqi":"更新于3天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170120/19193134_132840.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170120/19193134_132840.mp4","videoId":"2504","shareTItle":"上新了赶紧","videoNum":52,"riqi":"更新于3天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170116/19193134_141515.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170116/19193134_141515.mp4","videoId":"2505","shareTItle":"我发布了一个私房视频,快来围观","videoNum":52,"riqi":"更新于7天前|UFO"},{"userId":"43","nickName":"笑眯眯","headImg":"http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853","imgAddress":"http://img.toyihao.com/files/show20170116/19193134_122138.jpg","videoPlayAddress":"http://static.toyihao.com/files/show20170116/19193134_122138.mp4","videoId":"2506","shareTItle":"激情开始","videoNum":52,"riqi":"更新于7天前|UFO"}]
     * users : ["100","1140","1140","1140"]
     * videos : ["100","110"]
     */

    private int page;
    private int totalPage;
    private List<ConBean> con;
    private List<String> users;
    private List<String> videos;

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

    public static class ConBean {
        /**
         * userId : 43
         * nickName : 笑眯眯
         * headImg : http://static.toyihao.com/files/19190000/19193134.jpg?uptime=1473425853
         * imgAddress : http://img.toyihao.com/files/show20170122/19193134_172446.jpg
         * videoPlayAddress : http://static.toyihao.com/files/show20170122/19193134_172446.mp4
         * videoId : 12346
         * shareTItle : 来吧宝贝
         * videoNum : 52
         * riqi : 更新于1天前|UFO
         */

        private String userId;
        private String nickName;
        private String headImg;
        private String imgAddress;
        private String videoPlayAddress;
        private String videoId;
        private String shareTItle;
        private int videoNum;
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

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
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

        public int getVideoNum() {
            return videoNum;
        }

        public void setVideoNum(int videoNum) {
            this.videoNum = videoNum;
        }

        public String getRiqi() {
            return riqi;
        }

        public void setRiqi(String riqi) {
            this.riqi = riqi;
        }
    }
}
