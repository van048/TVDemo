package cn.ben.tvdemo.data.tvshow;

import java.util.List;

@SuppressWarnings("unused")
public class TVShows {
    private List<TVShow> result;

    private int error_code;

    private String reason;

    public void setResult(List<TVShow> result) {
        this.result = result;
    }

    public List<TVShow> getResult() {
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

    public static class TVShow {
        private String cName;

        private String pName;

        private String pUrl;

        private String time;

        private boolean isFav;

        public TVShow(String cn, String pn, String url, String t, boolean fav) {
            cName = cn;
            pName = pn;
            pUrl = url;
            time = t;
            isFav = fav;
        }

        public void setCName(String cName) {
            this.cName = cName;
        }

        public String getCName() {
            return this.cName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public String getPName() {
            return this.pName;
        }

        public void setPUrl(String pUrl) {
            this.pUrl = pUrl;
        }

        public String getPUrl() {
            return this.pUrl;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTime() {
            return this.time;
        }

        public boolean isFav() {
            return isFav;
        }

        public void setFav(boolean fav) {
            isFav = fav;
        }
    }
}
