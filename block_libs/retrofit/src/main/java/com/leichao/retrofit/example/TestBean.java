package com.leichao.retrofit.example;

import java.util.List;

public class TestBean {
    /**
     * count : 16
     * list : [{"id":"11e3653eddeb4bedba46ce99b1f4fa7d","title":"订单完成优惠券消息","messageType":1,"content":"1531845888","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-a2977c3dfada4cd19e79d95b49298e3d.html","createDate":"2018-09-04 18:54:08","messageSign":1},{"id":"3ee3c5bfc31d4ddd86fe808f838049cc","title":"优惠券消息","messageType":1,"content":"12132","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-d57f33227caf4d37a80334b728ff9cfe.html","createDate":"2018-08-07 10:17:46","messageSign":1},{"id":"ea559eac0e334b5093306990a409327e","title":"订单完成优惠券消息","messageType":2,"content":"优惠券消息","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-a2977c3dfada4cd19e79d95b49298e3d.html","createDate":"2018-07-26 16:53:06","messageSign":1},{"id":"b95e7da7cc8544998fd0d67bfd39364b","title":"测试","messageType":1,"content":"13590177954","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-80f4034a43c5424ba27c0c757cd8b321.html","createDate":"2018-05-03 10:34:16","messageSign":1},{"id":"ef36dc52f2814e36b95712883ff99bc0","title":"Hello","messageType":1,"content":"gfgg","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-761a0565091746a299825bfe1946a717.html","createDate":"2017-11-20 21:31:47","messageSign":1},{"id":"74ef5fe752f94aa28f7e885b8da9527a","title":"est","messageType":1,"content":"asf","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-92d9204f947c4f5ba6bcb244df101999.html","createDate":"2017-11-15 17:10:37","messageSign":1},{"id":"fbaee5893b3a43578469fb00c615fd48","title":"est","messageType":1,"content":"asdf","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-92d9204f947c4f5ba6bcb244df101999.html","createDate":"2017-11-08 19:06:07","messageSign":1},{"id":"93572671b8704973863fb39c5aa94124","title":"est","messageType":1,"content":"asfd","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-92d9204f947c4f5ba6bcb244df101999.html","createDate":"2017-11-08 19:05:46","messageSign":1},{"id":"6d1ec4b11c8e40a19b127065cd09e40e","title":"Driver Performance (Aug Week 1)","messageType":1,"content":"ebb","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-5ba9215a7bba4dd3928382317f7521ac.html","createDate":"2017-09-06 04:31:15","messageSign":1},{"id":"df5650e45482488788e2f2ab825f806b","title":"Promo link","messageType":1,"content":"顺丰到付","messageUrl":"f/view-3efb961da6f94e8083077406fc7682c3-35431d98f29648529972c077ac995ac6.html","createDate":"2017-09-03 21:00:54","messageSign":1}]
     */

    private int count;
    private List<ListBean> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 11e3653eddeb4bedba46ce99b1f4fa7d
         * title : 订单完成优惠券消息
         * messageType : 1
         * content : 1531845888
         * messageUrl : f/view-3efb961da6f94e8083077406fc7682c3-a2977c3dfada4cd19e79d95b49298e3d.html
         * createDate : 2018-09-04 18:54:08
         * messageSign : 1
         */

        private String id;
        private String title;
        private int messageType;
        private String content;
        private String messageUrl;
        private String createDate;
        private int messageSign;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getMessageType() {
            return messageType;
        }

        public void setMessageType(int messageType) {
            this.messageType = messageType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getMessageUrl() {
            return messageUrl;
        }

        public void setMessageUrl(String messageUrl) {
            this.messageUrl = messageUrl;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public int getMessageSign() {
            return messageSign;
        }

        public void setMessageSign(int messageSign) {
            this.messageSign = messageSign;
        }
    }
}
