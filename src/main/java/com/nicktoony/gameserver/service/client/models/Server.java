package com.nicktoony.gameserver.service.client.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 01/02/2015.
 */
public class Server {
    private int id;
    private String name;
    private int current_players;
    private int max_players;
    private HashMap<String, String> meta;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPlayers() {
        return current_players;
    }

    public int getMaxPlayers() {
        return max_players;
    }

    public Map<String, String> getMeta() {
        return meta;
    }
}
