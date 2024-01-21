package ru.thevalidator.daivinchikmatcher2.vk.dto;

import java.util.List;

public class LongPollServerResponse {

    private Integer ts;
    private Integer pts;
    private List<List<Object>> updates;
    private Integer failed;
    private Integer minVersion;
    private Integer maxVersion;

    public LongPollServerResponse() {
    }

    public Integer getTs() {
        return ts;
    }

    public void setTs(Integer ts) {
        this.ts = ts;
    }

    public Integer getPts() {
        return pts;
    }

    public void setPts(Integer pts) {
        this.pts = pts;
    }

    public List<List<Object>> getUpdates() {
        return updates;
    }

    public void setUpdates(List<List<Object>> updates) {
        this.updates = updates;
    }

    public Integer getFailed() {
        return failed;
    }

    public void setFailed(Integer failed) {
        this.failed = failed;
    }

    public Integer getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(Integer minVersion) {
        this.minVersion = minVersion;
    }

    public Integer getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(Integer maxVersion) {
        this.maxVersion = maxVersion;
    }

}
