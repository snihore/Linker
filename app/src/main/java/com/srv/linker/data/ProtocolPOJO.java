package com.srv.linker.data;

public class ProtocolPOJO {

    private String linkID;

    public ProtocolPOJO() {
    }

    public ProtocolPOJO(String linkID) {
        this.linkID = linkID;
    }

    public String getLinkID() {
        return linkID;
    }

    public void setLinkID(String linkID) {
        this.linkID = linkID;
    }
}
