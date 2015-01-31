package com.nicktoony.service;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Nick on 31/01/2015.
 */
public abstract class GameserverConfig {
    public static final String URL_CREATE_SERVER = "game/";

    private static GameserverConfig configuration;

    private OkHttpClient client;
    private Gson gson;

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
     * Provides a HTTP client instance
     * @return
     */
    public OkHttpClient getClient() {
        if (client == null ) {
            client = new OkHttpClient();
        }
        return client;
    }

    /**
     * Provides a GSon instance
     * @return
     */
    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }
}
