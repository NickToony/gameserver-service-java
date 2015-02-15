package com.nicktoony.gameserver.service.client.models;

import java.util.Map;

/**
 * Created by Nick on 01/02/2015.
 */
public class Server {
    private int id;
    private String name;
    private int currentPlayers;
    private int maxPlayers;
    private Map<String, String> meta;

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

    public Map<String, String> getMeta() {
        return meta;
    }
}
