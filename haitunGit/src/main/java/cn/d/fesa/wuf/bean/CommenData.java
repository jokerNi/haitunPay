package cn.d.fesa.wuf.bean;

import java.util.List;

/**
 * Created by schwager on 2016/7/25.
 */
public class CommenData {
    private List<OneVideoBean> one_video;
    private List<TopVideoBean> top_video;
    private List<CommentBean> comment;

    public List<OneVideoBean> getOne_video() {
        return one_video;
    }

    public void setOne_video(List<OneVideoBean> one_video) {
        this.one_video = one_video;
    }

    public List<TopVideoBean> getTop_video() {
        return top_video;
    }

    public void setTop_video(List<TopVideoBean> top_video) {
        this.top_video = top_video;
    }

    public List<CommentBean> getComment() {
        return comment;
    }

    public void setComment(List<CommentBean> comment) {
        this.comment = comment;
    }

    public static class OneVideoBean {
        /**
         * dianying_id : 1758
         * title : 颜が微かに浪呼んだ
         * video : http://www.hnxinlun.com/member_4.mp4
         * pic : http://www.hnxinlun.com/gold_4.jpg
         */

        private String dianying_id;
        private String title;
        private String video;
        private String pic;

        public String getDianying_id() {
            return dianying_id;
        }

        public void setDianying_id(String dianying_id) {
            this.dianying_id = dianying_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }

    public static class TopVideoBean {
        /**
         * dianying_id : 1758
         * title : 颜が微かに浪呼んだ
         * video : http://www.hnxinlun.com/member_4.mp4
         * pic : http://www.hnxinlun.com/gold_4.jpg
         */

        private String dianying_id;
        private String title;
        private String video;
        private String pic;

        public String getDianying_id() {
            return dianying_id;
        }

        public void setDianying_id(String dianying_id) {
            this.dianying_id = dianying_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }

    public static class CommentBean {
        /**
         * zan : 2712
         * content : 沙发捡肥皂，楼主耶
         * avatar : http://0731hz.com/img/pl/16.jpg
         * nickname : Dean、
         */

        private String zan;
        private String content;
        private String avatar;
        private String nickname;

        public String getZan() {
            return zan;
        }

        public void setZan(String zan) {
            this.zan = zan;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
