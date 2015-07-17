package com.nicktoony.gameserver.service.client;

import com.nicktoony.gameserver.service.Callback;
import com.nicktoony.gameserver.service.GameserverConfig;
import com.nicktoony.gameserver.service.client.models.Server;
import com.nicktoony.gameserver.service.client.responses.ServersList;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Nick on 01/02/2015.
 */
public class Client implements Callback {
    private List<Server> servers = new ArrayList<>();
    private boolean active = true;
    private ClientListener listener;

    /**
     * Simple listener object which can receive events
     */
    public interface ClientListener {
        public void onRefreshed();
        public void onFail();
    }

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
        GameserverConfig.getConfig().performGetRequest(GameserverConfig.getConfig().getServerUrl()
                        + GameserverConfig.URL_GET_SERVERS
                        + GameserverConfig.getConfig().getGameAPIKey()
                        + "?page=" + page,
                this);
    }

    @Override
    public void onFailure() {
        onFail();

        GameserverConfig.getConfig().debugLog(
                "FetchServers :: no response"
        );
    }

    @Override
    public void onSuccess(boolean success, int responseCode, String body) {
        try {
            ServersList serversList = GameserverConfig.getConfig().parseJsonForServerList(new StringReader(body));
            servers.addAll(Arrays.asList(serversList.getData()));

            GameserverConfig.getConfig().debugLog(
                    "FetchServers :: Fetched " + servers.size()
                            + " of " + serversList.getTotal() + " servers so far"
            );

            if (serversList.getCurrentPage() < serversList.getLastPage()) {
                fetch(serversList.getCurrentPage() + 1);
            } else {
                onRefresh();

                GameserverConfig.getConfig().debugLog(
                        "FetchServers :: Completed"
                );
            }
        } catch (IOException exception) {
            onFail();

            GameserverConfig.getConfig().debugLog(
                    "FetchServers :: Invalid JSON format"
            );
        }
    }

    private void onRefresh() {
        if (listener != null) {
            listener.onRefreshed();
        }
    }

    private void onFail() {
        active = false;
        if (listener != null) {
            listener.onFail();
        }
    }

    /**
     * Set a listener to receive key events
     * @param listener
     * @return
     */
    public Client setListener(ClientListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Returns the current servers fetched.. this might not be completed,
     * @return
     */
    public List<Server> getServers() {
        return servers;
    }
}
