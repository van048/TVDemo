package cn.ben.tvdemo.data.tvtype;

import java.util.List;

public class TVTypes {
    private List<TVType> result;

    private int error_code;

    private String reason;

    public void setResult(List<TVType> result) {
        this.result = result;
    }

    public List<TVType> getResult() {
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

    public static class TVType {
        private String id;

        private String name;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return this.id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public TVType(String i, String n) {
            id = i;
            name = n;
        }
    }
}
