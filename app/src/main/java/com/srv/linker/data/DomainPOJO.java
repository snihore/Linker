package com.srv.linker.data;

public class DomainPOJO {

    private String linkID;

    public DomainPOJO() {
    }

    public DomainPOJO(String linkID) {
        this.linkID = linkID;
    }

    public String getLinkID() {
        return linkID;
    }

    public void setLinkID(String linkID) {
        this.linkID = linkID;
    }
}
