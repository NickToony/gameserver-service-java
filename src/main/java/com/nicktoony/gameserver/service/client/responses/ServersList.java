package com.nicktoony.gameserver.service.client.responses;

import com.nicktoony.gameserver.service.client.models.Server;

/**
 * Created by Nick on 01/02/2015.
 */
public class ServersList {
    private Server[] data;
    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private int from;
    private int to;

    public Server[] getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }

    public int getPerPage() {
        return per_page;
    }

    public int getCurrentPage() {
        return current_page;
    }

    public int getLastPage() {
        return last_page;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public void setData(Server[] data) {
        this.data = data;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPerPage(int perPage) {
        this.per_page = perPage;
    }

    public void setCurrentPage(int currentPage) {
        this.current_page = currentPage;
    }

    public void setLastPage(int lastPage) {
        this.last_page = lastPage;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
