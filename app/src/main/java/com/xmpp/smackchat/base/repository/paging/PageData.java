package com.xmpp.smackchat.base.repository.paging;

import java.util.List;

public class PageData<T> {
    private int total;
    private List<T> items;

    public PageData(int total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public List<T> getItems() {
        return items;
    }
}
