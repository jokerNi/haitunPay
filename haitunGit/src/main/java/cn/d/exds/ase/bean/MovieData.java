package cn.d.exds.ase.bean;

import java.util.List;

/**
 * Created by schwager on 2016/6/22.
 */
public class MovieData {


    /**
     * con : [{"title":"身材苗条的辣妹拘束奸","pic":"http://520.szruiyaa.cn/1.jpg","video":"http://520.szruiyaa.cn/1.mp4"},{"title":"美容被下春药","pic":"http://520.szruiyaa.cn/2.jpg","video":"http://520.szruiyaa.cn/2.mp4"},{"title":"身材出众的美女","pic":"http://520.szruiyaa.cn/3.jpg","video":"http://520.szruiyaa.cn/3.mp4"},{"title":"卑猥的志愿者","pic":"http://520.szruiyaa.cn/4.jpg","video":"http://520.szruiyaa.cn/4.mp4"},{"title":"玩弄金发美少女","pic":"http://520.szruiyaa.cn/5.jpg","video":"http://520.szruiyaa.cn/5.mp4"},{"title":"爆乳娘潮吹","pic":"http://520.szruiyaa.cn/6.jpg","video":"http://520.szruiyaa.cn/6.mp4"},{"title":"完美的爆乳美女","pic":"http://520.szruiyaa.cn/7.jpg","video":"http://520.szruiyaa.cn/7.mp4"},{"title":"丁字裤美女淫乱","pic":"http://520.szruiyaa.cn/8.jpg","video":"http://520.szruiyaa.cn/8.mp4"},{"title":"街头巷尾淫乱妻","pic":"http://520.szruiyaa.cn/9.jpg","video":"http://520.szruiyaa.cn/9.mp4"}]
     * page : 1
     * totalPage : 1top : [{"title":"身材苗条的辣妹拘束奸","pic":"http://520.szruiyaa.cn/1.jpg","video":"http://520.szruiyaa.cn/1.mp4"},{"title":"美容被下春药","pic":"http://520.szruiyaa.cn/2.jpg","video":"http://520.szruiyaa.cn/2.mp4"},{"title":"身材出众的美女","pic":"http://520.szruiyaa.cn/3.jpg","video":"http://520.szruiyaa.cn/3.mp4"}]
     */
    private List<ConEntity> con;
    private int page;
    private int totalPage;
    private List<TopEntity> top;

    public void setCon(List<ConEntity> con) {
        this.con = con;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void setTop(List<TopEntity> top) {
        this.top = top;
    }

    public List<ConEntity> getCon() {
        return con;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<TopEntity> getTop() {
        return top;
    }

    public class ConEntity {
        /**
         * title : 身材苗条的辣妹拘束奸
         * pic : http://520.szruiyaa.cn/1.jpg
         * video : http://520.szruiyaa.cn/1.mp4
         */
        private String title;
        private String pic;
        private String video;
        private String id;
        private String dianying_id;


        public String getDianying_id() {
            return dianying_id;
        }

        public void setDianying_id(String dianying_id) {
            this.dianying_id = dianying_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getPic() {
            return pic;
        }

        public String getVideo() {
            return video;
        }
    }

    public class TopEntity {
        /**
         * title : 身材苗条的辣妹拘束奸
         * pic : http://520.szruiyaa.cn/1.jpg
         * video : http://520.szruiyaa.cn/1.mp4
         */
        private String title;
        private String pic;
        private String video;

        public void setTitle(String title) {
            this.title = title;
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

        public String getPic() {
            return pic;
        }

        public String getVideo() {
            return video;
        }
    }
}
