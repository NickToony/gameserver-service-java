package com.nicktoony.gameserver.service;

import com.nicktoony.gameserver.service.client.responses.ServersList;
import com.nicktoony.gameserver.service.host.APIResponse.CreateServer;
import com.nicktoony.gameserver.service.host.APIResponse.UpdateServer;
import com.nicktoony.gameserver.service.host.Host;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * Created by Nick on 31/01/2015.
 */
public abstract class GameserverConfig {
    public static final String URL_CREATE_SERVER = "game/";
    public static final String URL_UPDATE_SERVER = "game/";
    public static final String URL_GET_SERVERS = "game/";

    private static GameserverConfig configuration;

    /**
     * Set your own custom GameserverConfig option. You should extend this class to implement it.
     * @param config
     */
    public static void setConfig(GameserverConfig config) {
        configuration = config;
    }

    /**
     * Gets the configuration object in use
     * @return
     */
    public static GameserverConfig getConfig() {
        return configuration;
    }

    /**
     * The root server url.
     * E.g. http://gameserver.co.uk/api/
     * @return
     */
    public abstract String getServerUrl();

    /**
     * The game's API key
     * e.g. Qn27pm5qDUSZWAxHc8twjapewvC6fSD98SmdwrAGu6fZ9uUmQhTajB32BE4V
     * @return
     */
    public abstract String getGameAPIKey();

    /**
     * Handle debug messages here as you see fit
     * @param message
     */
    public abstract void debugLog(String message);

    /**
     * The rate at which to send updates to the server, in milliseconds
     * e.g. 2 * 60 * 1000 would be 2 minutes
     * @return
     */
    public abstract long getUpdateRate();

    /**
     * Not used yet
     * @return
     */
    public abstract long getChangedUpdateRate();

    /**
     * Given the body of a response, create a ServerList model
     * @param reader
     * @return the completed model
     * @throws IOException
     */
    public abstract ServersList parseJsonForServerList(Reader reader) throws IOException;

    /**
     * Given the body of a response, create a CreateServer model
     * @param reader
     * @return the completed model
     * @throws IOException
     */
    public abstract CreateServer parseJsonForCreateServer(Reader reader) throws IOException;

    /**
     * Given the body of a response, create a UpdateServer model
     * @param reader
     * @return the completed model
     * @throws IOException
     */
    public abstract UpdateServer parseJsonForUpdateServer(Reader reader) throws IOException;


    public abstract void performGetRequest(String url, Callback callback);

    public abstract void performPostRequest(String url, Map<String, String> data, Callback callback);

    public abstract void startHostLoop(Host host, long rate);
    public abstract void endHostLoop(Host host);
}
