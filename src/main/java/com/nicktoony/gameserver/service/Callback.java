package com.nicktoony.gameserver.service;

import java.io.IOException;

/**
 * Created by nick on 16/07/15.
 */
public interface Callback {
    public void onSuccess(boolean success, int responseCode, String body);
    public void onFailure();
}
