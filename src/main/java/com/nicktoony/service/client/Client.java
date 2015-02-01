package com.nicktoony.service.client;

import com.google.gson.JsonSyntaxException;
import com.nicktoony.service.GameserverConfig;
import com.nicktoony.service.client.models.Server;
import com.nicktoony.service.client.responses.ServersList;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nick on 01/02/2015.
 */
public class Client implements Callback {
    private List<Server> servers = new ArrayList<>();
    private boolean active = true;

    /**
     * Wipe the current server list, and fetch a fresh one
     */
    public void refresh() {
        if (!active) {
            return;
        }

        servers.clear();
        fetch(1);
    }

    /**
     * Internal method for making the GET server list call
     * this is called once for each page
     * @param page
     */
    private void fetch(int page) {
        Request request = new Request.Builder()
                .url(GameserverConfig.getConfig().getServerUrl()
                        + GameserverConfig.URL_GET_SERVERS
                        + GameserverConfig.getConfig().getGameAPIKey()
                        + "?page=" + page)
                .get()
                .build();

        GameserverConfig.getConfig().getClient().newCall(request).enqueue(this);
    }

    @Override
    public void onFailure(Request request, IOException e) {
        active = false;

        GameserverConfig.getConfig().debugLog(
                "FetchServers :: no response"
        );
    }

    @Override
    public void onResponse(Response response) throws IOException {
        try {
            ServersList serversList = GameserverConfig.getConfig().getGson()
                    .fromJson(response.body().charStream(), ServersList.class);
            servers.addAll(Arrays.asList(serversList.getData()));

            GameserverConfig.getConfig().debugLog(
                    "FetchServers :: Fetched " + servers.size()
                            + " of " + serversList.getTotal() + " servers so far"
            );

            if (serversList.getCurrentPage() < serversList.getLastPage()) {
                fetch(serversList.getCurrentPage() + 1);
            } else {
                GameserverConfig.getConfig().debugLog(
                        "FetchServers :: Completed"
                );
            }
        } catch (JsonSyntaxException exception) {
            GameserverConfig.getConfig().debugLog(
                    "FetchServers :: Invalid JSON format"
            );
        }
    }
}
