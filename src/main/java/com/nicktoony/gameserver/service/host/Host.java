package com.nicktoony.gameserver.service.host;

import com.google.gson.JsonSyntaxException;
import com.nicktoony.gameserver.service.GameserverConfig;
import com.nicktoony.gameserver.service.host.APIResponse.CreateServer;
import com.nicktoony.gameserver.service.host.APIResponse.UpdateServer;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
    private String password;
    private String id;
    private Timer timer;
    private TimerTask timerTask;

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

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                step();
            }
        };

        // send to gameserver list
        //create();
    }

    /**
     * The inner loop of the timer task. It will stop itself when the server is no longer active.
     */
    private void step() {
        // immediately stop if the server is no longer active
        if (!active) {
            timer.cancel();
            return;
        }

        // Create the form, attaching meta
        FormEncodingBuilder formBuilder = new FormEncodingBuilder()
                .add("name", name)
                .add("current_players", Integer.toString(currentPlayers))
                .add("max_players", Integer.toString(maxPlayers))
                .add("password", password);
        attachMeta(formBuilder);
        // Create request
        Request request = new Request.Builder()
                .url(GameserverConfig.getConfig().getServerUrl()
                        + GameserverConfig.URL_UPDATE_SERVER
                        + GameserverConfig.getConfig().getGameAPIKey()
                        + "/" + id)
                .post(formBuilder.build())
                .build();
        GameserverConfig.getConfig().getClient().newCall(request).enqueue(this);

        // reschedule the timer
        //timer.schedule(timerTask, GameserverConfig.getConfig().getUpdateRate());
    }

    /**
     * Method which performs the CREATE_SERVER request to the API. It handles the response
     * and starts the timer task
     */
    public void create() {

        // Create the form, attaching meta
        FormEncodingBuilder formBuilder = new FormEncodingBuilder()
                .add("name", name)
                .add("current_players", Integer.toString(currentPlayers))
                .add("max_players", Integer.toString(maxPlayers));
        attachMeta(formBuilder);

        // Create the request
        Request request = new Request.Builder()
                .url(GameserverConfig.getConfig().getServerUrl()
                        + GameserverConfig.URL_CREATE_SERVER
                        + GameserverConfig.getConfig().getGameAPIKey())
                .post(formBuilder.build())
                .build();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                GameserverConfig.getConfig().debugLog(
                        "CreateServer :: Failed to connect"
                );
            }

            @Override
            public void onResponse(com.squareup.okhttp.Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        // Read the JSON response
                        CreateServer server = GameserverConfig.getConfig().getGson()
                                .fromJson(response.body().charStream(), CreateServer.class);

                        if (server.isSuccess()) {
                            active = true;
                            password = server.getPassword(); // we need this for updating
                            id = server.getId(); // we need this for updating
                            timer.scheduleAtFixedRate(timerTask, // start the timer task
                                    GameserverConfig.getConfig().getUpdateRate(),
                                    GameserverConfig.getConfig().getUpdateRate());

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
                    } catch (JsonSyntaxException exception) {
                        GameserverConfig.getConfig().debugLog(
                                "CreateServer :: Invalid JSON format"
                        );
                    }

                } else {
                    GameserverConfig.getConfig().debugLog(
                            "CreateServer :: HTTP header failure: " + response.code()
                    );
                }
            }
        };

        // Finally, make the call (ASYNC)
        GameserverConfig.getConfig().getClient().newCall(request).enqueue(callback);

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
    public void onFailure(Request request, IOException e) {
        active = false;
        GameserverConfig.getConfig().debugLog(
                "CreateServer :: Failed to connect"
        );
    }

    @Override
    public void onResponse(com.squareup.okhttp.Response response) throws IOException {
        if (response.isSuccessful()) {
            try {
                // Read the json response
                UpdateServer server = GameserverConfig.getConfig().getGson()
                        .fromJson(response.body().charStream(), UpdateServer.class);

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
            } catch (JsonSyntaxException exception) {
                active = false;
                GameserverConfig.getConfig().debugLog(
                        "UpdateServer :: Invalid JSON format"
                );
            }

        } else {
            active = false;
            GameserverConfig.getConfig().debugLog(
                    "UpdateServer :: HTTP header failure: " + response.code()
            );
        }
    }

    public void addMeta(String key, String value) {
        meta.put(key, value);
    }

    private void attachMeta(FormEncodingBuilder builder) {
        for (Map.Entry<String, String> data : meta.entrySet()) {
            builder.add(data.getKey(), data.getValue());
        }
    }
}
