package com.nicktoony.gameserver.service.host;

import com.nicktoony.gameserver.service.Callback;
import com.nicktoony.gameserver.service.GameserverConfig;
import com.nicktoony.gameserver.service.host.APIResponse.CreateServer;
import com.nicktoony.gameserver.service.host.APIResponse.UpdateServer;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 31/01/2015.
 *
 * This class is for use by servers wishing to keep their details up to date
 */
public class Host implements Callback {

    private String name;
    private int currentPlayers;
    private int maxPlayers;

    private boolean changed = false;
    private boolean active = false;
    private long lastUpdate = 0;
    private String password;
    private String id;

    private Map<String, String> meta;

    /**
     * Create a new host on the server list
     * @param name
     * @param currentPlayers
     * @param maxPlayers
     */
    public Host(String name, int currentPlayers, int maxPlayers) {
        this.name = name;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;

        meta = new HashMap<>();

        // send to gameserver list
        //create();
    }

    /**
     * The inner loop of the timer task. It will stop itself when the server is no longer active.
     */
    public void step() {
        // immediately stop if the server is no longer active
        if (!active) {
<<<<<<< HEAD
            GameserverConfig.getConfig().endHostLoop(this);
=======
>>>>>>> Removed timer usage. You should call step yourself in your server loop.
            return;
        }

        // Only push updates when we should
        if (lastUpdate + GameserverConfig.getConfig().getUpdateRate() > System.currentTimeMillis()) {
            return;
        }
        lastUpdate = System.currentTimeMillis();

        // Create the form, attaching meta
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("name", name);
        parameters.put("current_players", Integer.toString(currentPlayers));
        parameters.put("max_players", Integer.toString(maxPlayers));
        parameters.put("password", password);
        attachMeta(parameters);
        // Create request

        GameserverConfig.getConfig().performPostRequest(GameserverConfig.getConfig().getServerUrl()
                + GameserverConfig.URL_UPDATE_SERVER
                + GameserverConfig.getConfig().getGameAPIKey()
                + "/" + id,
                parameters,
                this);
    }

    /**
     * Method which performs the CREATE_SERVER request to the API. It handles the response
     * and starts the timer task
     */
    public void create() {

        Callback callback = new Callback() {
            @Override
            public void onFailure() {
                GameserverConfig.getConfig().debugLog(
                        "CreateServer :: Failed to connect"
                );
            }

            @Override
            public void onSuccess(boolean success, int responseCode, String body) {
                if (success) {
                    try {
                        // Read the JSON response
                        CreateServer server = GameserverConfig.getConfig().parseJsonForCreateServer(new StringReader(body));

                        if (server.isSuccess()) {
                            active = true;
                            password = server.getPassword(); // we need this for updating
                            id = server.getId(); // we need this for updating
<<<<<<< HEAD
//                            timer.scheduleAtFixedRate(timerTask, // start the timer task
//                                    GameserverConfig.getConfig().getUpdateRate(),
//                                    GameserverConfig.getConfig().getUpdateRate());
                            GameserverConfig.getConfig().startHostLoop(Host.this, GameserverConfig.getConfig().getUpdateRate());
=======
                            lastUpdate = System.currentTimeMillis();
>>>>>>> Removed timer usage. You should call step yourself in your server loop.

                            GameserverConfig.getConfig().debugLog(
                                    "CreateServer :: Success"
                            );
                        } else {
                            GameserverConfig.getConfig().debugLog(
                                    "CreateServer :: Server responded with a failure:"
                            );

                            // DEBUG: Output all errors
                            for (Map.Entry<String, String[]> error : server.getErrors().entrySet()) {
                                GameserverConfig.getConfig().debugLog(
                                        "    " + error.getKey() + " - " + error.getValue()[0]
                                );
                            }
                        }
                    } catch (IOException exception) {
                        GameserverConfig.getConfig().debugLog(
                                "CreateServer :: Invalid JSON format"
                        );
                    }

                } else {
                    GameserverConfig.getConfig().debugLog(
                            "CreateServer :: HTTP header failure: " + responseCode
                    );
                }
            }
        };


        // Create the form, attaching meta
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("name", name);
        parameters.put("current_players", Integer.toString(currentPlayers));
        parameters.put("max_players", Integer.toString(maxPlayers));
        attachMeta(parameters);

        // Finally, make the call (ASYNC)
        GameserverConfig.getConfig().performPostRequest(GameserverConfig.getConfig().getServerUrl()
                + GameserverConfig.URL_CREATE_SERVER
                + GameserverConfig.getConfig().getGameAPIKey(),
                parameters,
                callback);

    }

    /**
     * Tells the host to stop updating itself, meaning it'll be removed from the server list
     */
    public void stop() {
        this.active = false;
    }

    /**
     * If the server is inactive, it has either had an error, or shut down itself
     * @return
     */
    public boolean isActive() {
        return active;
    }

    public Host setName(String name) {
        this.name = name;
        changed = true;
        return this;
    }

    public Host setCurrentPlayers(int players) {
        this.currentPlayers = players;
        changed = true;
        return this;
    }

    public Host setMaxPlayers(int players) {
        this.maxPlayers = players;
        changed = true;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public void onFailure() {
        active = false;
        GameserverConfig.getConfig().debugLog(
                "CreateServer :: Failed to connect"
        );
    }

    @Override
    public void onSuccess(boolean success, int responseCode, String body) {
        if (success) {
            try {
                // Read the json response
                UpdateServer server = GameserverConfig.getConfig().parseJsonForUpdateServer(new StringReader(body));

                if (server.isSuccess()) {
                    GameserverConfig.getConfig().debugLog(
                            "UpdateServer :: Success"
                    );
                } else {
                    active = false;
                    GameserverConfig.getConfig().debugLog(
                            "UpdateServer :: Server responded with a failure:"
                    );

                    // DEBUG: Output all errors
                    for (Map.Entry<String, String[]> error : server.getErrors().entrySet()) {
                        GameserverConfig.getConfig().debugLog(
                                "    " + error.getKey() + " - " + error.getValue()[0]
                        );
                    }
                }
            } catch (IOException exception) {
                active = false;
                GameserverConfig.getConfig().debugLog(
                        "UpdateServer :: Invalid JSON format"
                );
            }

        } else {
            active = false;
            GameserverConfig.getConfig().debugLog(
                    "UpdateServer :: HTTP header failure: " + responseCode
            );
        }
    }

    public void addMeta(String key, String value) {
        meta.put(key, value);
    }

    private void attachMeta(Map<String, String> map) {
        map.putAll(meta);
    }
}
