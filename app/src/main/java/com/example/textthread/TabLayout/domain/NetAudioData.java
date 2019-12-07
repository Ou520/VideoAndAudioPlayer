package com.example.textthread.TabLayout.domain;

//网络音乐的数据

import java.util.List;

public class NetAudioData {
    /**
     * count : 4199
     * np : 1.469548322E9
     */

    private InfoEntity info;
    /**
     * status : 4
     * comment : 68
     * tags : [{"id":55,"name":"微视频"},{"id":117,"name":"美女"},{"id":10087,"name":"才艺"}]
     * bookmark : 151
     * text : 校园疯狂版 “咋了爸爸”！有几个肯陪你疯的同学真好
     * up : 817
     * share_url : http://www.budejie.com/share/19511191.html?wx.qq.com
     * down : 89
     * forward : 186
     * u : {"header":["http://wimg.spriteapp.cn/profile/large/2016/07/07/577e6fba2b730_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/07/07/577e6fba2b730_mini.jpg"],"is_v":true,"uid":"6172490","is_vip":true,"name":"轉身丶看不見你的陽光傾城"}
     * passtime : 2016-07-27 08:43:01
     * video : {"playfcount":1439,"height":236,"width":426,"video":["http://wvideo.spriteapp.cn/video/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpd.mp4","http://bvideo.spriteapp.cn/video/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpd.mp4"],"duration":133,"playcount":40036,"thumbnail":["http://wimg.spriteapp.cn/picture/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpd_74.jpg","http://dimg.spriteapp.cn/picture/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpd_74.jpg"],"download":["http://wvideo.spriteapp.cn/video/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpc.mp4","http://bvideo.spriteapp.cn/video/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpc.mp4"]}
     * type : video
     * id : 19511191
     */

    private List<ListEntity> list;

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class InfoEntity {
        private int count;
        private double np;

        public void setCount(int count) {
            this.count = count;
        }

        public void setNp(double np) {
            this.np = np;
        }

        public int getCount() {
            return count;
        }

        public double getNp() {
            return np;
        }
    }

    public static class ListEntity {
        private int status;
        private String comment;
        private String bookmark;
        private String text;
        private String up;
        private String share_url;
        private int down;
        private int forward;
        /**
         * header : ["http://wimg.spriteapp.cn/profile/large/2016/07/07/577e6fba2b730_mini.jpg","http://dimg.spriteapp.cn/profile/large/2016/07/07/577e6fba2b730_mini.jpg"]
         * is_v : true
         * uid : 6172490
         * is_vip : true
         * name : 轉身丶看不見你的陽光傾城
         */

        private UEntity u;
        private String passtime;
        /**
         * playfcount : 1439
         * height : 236
         * width : 426
         * video : ["http://wvideo.spriteapp.cn/video/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpd.mp4","http://bvideo.spriteapp.cn/video/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpd.mp4"]
         * duration : 133
         * playcount : 40036
         * thumbnail : ["http://wimg.spriteapp.cn/picture/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpd_74.jpg","http://dimg.spriteapp.cn/picture/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpd_74.jpg"]
         * download : ["http://wvideo.spriteapp.cn/video/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpc.mp4","http://bvideo.spriteapp.cn/video/2016/0724/c5e99504-519b-11e6-8bd6-d4ae5296039d_wpc.mp4"]
         */

        private VideoEntity video;
        /**
         * 图片
         */
        private ImageEntity image;

        private GifEntity gif;
        private String type;
        private String id;

        public GifEntity getGif() {
            return gif;
        }

        public void setGif(GifEntity gif) {
            this.gif = gif;
        }

        public ImageEntity getImage() {
            return image;
        }

        public void setImage(ImageEntity image) {
            this.image = image;
        }

        /**
         * id : 55
         * name : 微视频
         */

        private List<TagsEntity> tags;

        public void setStatus(int status) {
            this.status = status;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setBookmark(String bookmark) {
            this.bookmark = bookmark;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public void setDown(int down) {
            this.down = down;
        }

        public void setForward(int forward) {
            this.forward = forward;
        }

        public void setU(UEntity u) {
            this.u = u;
        }

        public void setPasstime(String passtime) {
            this.passtime = passtime;
        }

        public void setVideo(VideoEntity video) {
            this.video = video;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTags(List<TagsEntity> tags) {
            this.tags = tags;
        }

        public int getStatus() {
            return status;
        }

        public String getComment() {
            return comment;
        }

        public String getBookmark() {
            return bookmark;
        }

        public String getText() {
            return text;
        }

        public String getUp() {
            return up;
        }

        public String getShare_url() {
            return share_url;
        }

        public int getDown() {
            return down;
        }

        public int getForward() {
            return forward;
        }

        public UEntity getU() {
            return u;
        }

        public String getPasstime() {
            return passtime;
        }

        public VideoEntity getVideo() {
            return video;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }

        public List<TagsEntity> getTags() {
            return tags;
        }

        public static class UEntity {
            private boolean is_v;
            private String uid;
            private boolean is_vip;
            private String name;
            private List<String> header;

            public void setIs_v(boolean is_v) {
                this.is_v = is_v;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public void setIs_vip(boolean is_vip) {
                this.is_vip = is_vip;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setHeader(List<String> header) {
                this.header = header;
            }

            public boolean isIs_v() {
                return is_v;
            }

            public String getUid() {
                return uid;
            }

            public boolean isIs_vip() {
                return is_vip;
            }

            public String getName() {
                return name;
            }

            public List<String> getHeader() {
                return header;
            }
        }

        public static class GifEntity{

            /**
             * images : ["http://ww4.sinaimg.cn/large/005OPWbujw1f5y7y68fyyg307s055npd.gif","http://wimg.spriteapp.cn/ugc/2016/07/18/578c76fab0bab.gif","http://dimg.spriteapp.cn/ugc/2016/07/18/578c76fab0bab.gif"]
             * width : 280
             * gif_thumbnail : ["http://wimg.spriteapp.cn/ugc/2016/07/18/578c76fab0bab_a_1.jpg","http://dimg.spriteapp.cn/ugc/2016/07/18/578c76fab0bab_a_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2016/07/18/578c76fab0bab_d.jpg","http://dimg.spriteapp.cn/ugc/2016/07/18/578c76fab0bab_d.jpg","http://wimg.spriteapp.cn/ugc/2016/07/18/578c76fab0bab_a_1.jpg","http://dimg.spriteapp.cn/ugc/2016/07/18/578c76fab0bab_a_1.jpg"]
             * height : 185
             */

            private int width;
            private int height;
            private List<String> images;
            private List<String> gif_thumbnail;
            private List<String> download_url;

            public void setWidth(int width) {
                this.width = width;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }

            public void setGif_thumbnail(List<String> gif_thumbnail) {
                this.gif_thumbnail = gif_thumbnail;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public List<String> getImages() {
                return images;
            }

            public List<String> getGif_thumbnail() {
                return gif_thumbnail;
            }

            public List<String> getDownload_url() {
                return download_url;
            }
        }

        public static class ImageEntity {

            /**
             * medium : ["http://ww1.sinaimg.cn/bmiddle/c1e8ffd5jw1f67bs2slppj20e423iq8z.jpg"]
             * big : ["http://ww1.sinaimg.cn/large/c1e8ffd5jw1f67bs2slppj20e423iq8z.jpg","http://wimg.spriteapp.cn/ugc/2016/07/21/5790a95ac86c2_1.jpg","http://dimg.spriteapp.cn/ugc/2016/07/21/5790a95ac86c2_1.jpg"]
             * download_url : ["http://wimg.spriteapp.cn/ugc/2016/07/21/5790a95ac86c2_d.jpg","http://dimg.spriteapp.cn/ugc/2016/07/21/5790a95ac86c2_d.jpg","http://wimg.spriteapp.cn/ugc/2016/07/21/5790a95ac86c2.jpg","http://dimg.spriteapp.cn/ugc/2016/07/21/5790a95ac86c2.jpg"]
             * height : 2718
             * width : 508
             * small : ["http://ww1.sinaimg.cn/mw240/c1e8ffd5jw1f67bs2slppj20e423iq8z.jpg"]
             */

            private int height;
            private int width;
            private List<String> medium;
            private List<String> big;
            private List<String> download_url;
            private List<String> small;

            public void setHeight(int height) {
                this.height = height;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public void setMedium(List<String> medium) {
                this.medium = medium;
            }

            public void setBig(List<String> big) {
                this.big = big;
            }

            public void setDownload_url(List<String> download_url) {
                this.download_url = download_url;
            }

            public void setSmall(List<String> small) {
                this.small = small;
            }

            public int getHeight() {
                return height;
            }

            public int getWidth() {
                return width;
            }

            public List<String> getMedium() {
                return medium;
            }

            public List<String> getBig() {
                return big;
            }

            public List<String> getDownload_url() {
                return download_url;
            }

            public List<String> getSmall() {
                return small;
            }
        }

        public static class VideoEntity {
            private int playfcount;
            private int height;
            private int width;
            private int duration;
            private int playcount;
            private List<String> video;
            private List<String> thumbnail;
            private List<String> download;

            public void setPlayfcount(int playfcount) {
                this.playfcount = playfcount;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public void setPlaycount(int playcount) {
                this.playcount = playcount;
            }

            public void setVideo(List<String> video) {
                this.video = video;
            }

            public void setThumbnail(List<String> thumbnail) {
                this.thumbnail = thumbnail;
            }

            public void setDownload(List<String> download) {
                this.download = download;
            }

            public int getPlayfcount() {
                return playfcount;
            }

            public int getHeight() {
                return height;
            }

            public int getWidth() {
                return width;
            }

            public int getDuration() {
                return duration;
            }

            public int getPlaycount() {
                return playcount;
            }

            public List<String> getVideo() {
                return video;
            }

            public List<String> getThumbnail() {
                return thumbnail;
            }

            public List<String> getDownload() {
                return download;
            }
        }

        public static class TagsEntity {
            private int id;
            private String name;

            public void setId(int id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }
        }
    }
}
