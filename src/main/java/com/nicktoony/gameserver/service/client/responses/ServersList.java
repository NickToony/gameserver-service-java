package com.nicktoony.gameserver.service.client.responses;

import com.nicktoony.gameserver.service.client.models.Server;

/**
 * Created by Nick on 01/02/2015.
 */
public class ServersList {
    private Server[] data;
    private int total;
    private int perPage;
    private int currentPage;
    private int lastPage;
    private int from;
    private int to;

    public Server[] getData() {
        return data;
    }

    public int getTotal() {
        return total;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLastPage() {
        return lastPage;
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
        this.perPage = perPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }
}
