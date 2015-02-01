package com.nicktoony.service.client.models;

/**
 * Created by Nick on 01/02/2015.
 */
public class Server {
    private int id;
    private String name;
    private int currentPlayers;
    private int maxPlayers;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}
