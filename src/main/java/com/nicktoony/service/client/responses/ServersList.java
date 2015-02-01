package com.nicktoony.service.client.responses;

import com.nicktoony.service.client.models.Server;

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
}
