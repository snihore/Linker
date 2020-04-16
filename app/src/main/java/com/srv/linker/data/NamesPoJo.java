package com.srv.linker.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NamesPoJo {

    private List<String> list;

    public NamesPoJo() {
    }

    public NamesPoJo(String name) {

        this.list = new ArrayList<>();

        this.list.add(name);
    }

    public void setName(String name){

        Set<String> set = new HashSet<>(this.list);

        set.add(name);

        this.list = new ArrayList<>(set);
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
