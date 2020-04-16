package com.srv.linker.data;

import java.util.Date;
import java.util.UUID;

public class Link {

    private String urlName;
    private long timestamp;
    private String urlTag;
    private String uniqueID;

    public Link() {
    }

    public Link(String urlName) throws Exception{
        this.urlName = urlName;
        this.urlTag = "";
        this.uniqueID = UUID.randomUUID().toString();
        timestamp = new Date().getTime();

    }

    public Link(String urlName, String urlTag) throws Exception{
        this.urlName = urlName;
        this.urlTag = urlTag;
        this.uniqueID = UUID.randomUUID().toString();
        timestamp = new Date().getTime();
    }

    public void setUrlTag(String urlTag) {
        this.urlTag = urlTag;
    }

    public String getUrlName() {
        return urlName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUrlTag() {
        return urlTag;
    }

    public String getUniqueID() {
        return uniqueID;
    }
}
