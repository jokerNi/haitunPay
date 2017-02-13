package cn.d.yurfe.bgc.bean;

import java.util.List;

/**
 * Created by schwager on 2016/7/25.
 */
public class CommenData {

    /**
     * top_video : [{"title":"女阴满足感","dianying_id":"2","pic":"http://o88b2h1yr.bkt.clouddn.com/img/zstp2.jpg","video":""},{"title":"可爱娃娃SEX","pic":"http://o88b2h1yr.bkt.clouddn.com/img/zstp202.jpg","video":""},{"title":"不懂事故上京娘","pic":"http://o88b2h1yr.bkt.clouddn.com/img/zstp122.jpg","video":""},{"title":"新潮素人4时间","pic":"http://o88b2h1yr.bkt.clouddn.com/img/zstp177.jpg","video":""}]
     * comment : [{"content":"我建议LZ找那个最粗的！","nickname":"低调潜行","zan":"3151","avatar":"http://imgtu.chnhtp.com:8081/XinChangPing225/resources/avatars/3.png"},{"content":"搞起来爽啊","nickname":"Rico","zan":"5494","avatar":"http://imgtu.chnhtp.com:8081/XinChangPing225/resources/avatars/4.png"},{"content":"虾兵蟹将举行","nickname":"飞虎队简简单单就","zan":"1103","avatar":"http://api.feilaizhe.net/backend/web/resources/avatars/19Bl20160325160705.jpg"},{"content":"这个尺度好大~","nickname":"没温度的娃娃","zan":"2359","avatar":"http://imgtu.chnhtp.com:8081/XinChangPing225/resources/avatars/5.png"},{"content":"你个傻逼 ","nickname":"飞虎队简简单单就","zan":"3306","avatar":"http://api.feilaizhe.net/backend/web/resources/avatars/19Bl20160325160705.jpg"},{"content":"想上你","nickname":"随风","zan":"6381","avatar":"http://imgtu.chnhtp.com:8081/XinChangPing225/resources/avatars/6.png"},{"content":"你好","nickname":"gigue","zan":"1826","avatar":"http://api.feilaizhe.net/backend/web/resources/avatars/1yd120160307231810.jpg"},{"content":"后入的冲动","nickname":"根本停不下来","zan":"9558","avatar":"http://imgtu.chnhtp.com:8081/XinChangPing225/resources/avatars/7.png"},{"content":"福利啊福利啊","nickname":"笑三少","zan":"3137","avatar":"http://imgtu.chnhtp.com:8081/XinChangPing225/resources/avatars/8.png"},{"content":"者屁股玩一晚上都不会累","nickname":"太阳雨","zan":"9015","avatar":"http://imgtu.chnhtp.com:8081/XinChangPing225/resources/avatars/9.png"}]
     */
    private List<Top_videoEntity> top_video;
    private List<CommentEntity> comment;

    public void setTop_video(List<Top_videoEntity> top_video) {
        this.top_video = top_video;
    }

    public void setComment(List<CommentEntity> comment) {
        this.comment = comment;
    }

    public List<Top_videoEntity> getTop_video() {
        return top_video;
    }

    public List<CommentEntity> getComment() {
        return comment;
    }

    public class Top_videoEntity {
        /**
         * title : 女阴满足感
         * dianying_id : 2
         * pic : http://o88b2h1yr.bkt.clouddn.com/img/zstp2.jpg
         * video :
         */
        private String title;
        private String dianying_id;
        private String pic;
        private String video;

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDianying_id(String dianying_id) {
            this.dianying_id = dianying_id;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getTitle() {
            return title;
        }

        public String getDianying_id() {
            return dianying_id;
        }

        public String getPic() {
            return pic;
        }

        public String getVideo() {
            return video;
        }
    }

    public class CommentEntity {
        /**
         * content : 我建议LZ找那个最粗的！
         * nickname : 低调潜行
         * zan : 3151
         * avatar : http://imgtu.chnhtp.com:8081/XinChangPing225/resources/avatars/3.png
         */
        private String content;
        private String nickname;
        private String zan;
        private String avatar;

        public void setContent(String content) {
            this.content = content;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setZan(String zan) {
            this.zan = zan;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getContent() {
            return content;
        }

        public String getNickname() {
            return nickname;
        }

        public String getZan() {
            return zan;
        }

        public String getAvatar() {
            return avatar;
        }
    }
}
