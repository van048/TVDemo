package cn.ben.tvdemo.data.tvchannel;

import java.util.List;

@SuppressWarnings("unused")
public class TVChannels {
    private List<TVChannel> result;

    private int error_code;

    private String reason;

    public void setResult(List<TVChannel> result) {
        this.result = result;
    }

    public List<TVChannel> getResult() {
        return this.result;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getError_code() {
        return this.error_code;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    @SuppressWarnings("unused")
    public static class TVChannel {
        private String channelName;

        private String pId;

        private String rel;

        private String url;

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public String getChannelName() {
            return this.channelName;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public String getPId() {
            return this.pId;
        }

        public void setRel(String rel) {
            this.rel = rel;
        }

        public String getRel() {
            return this.rel;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }

        public TVChannel(String id, String name, String rel, String url) {
            this.pId = id;
            this.channelName = name;
            this.rel = rel;
            this.url = url;
        }
    }
}
