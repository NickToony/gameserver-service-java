package com.nicktoony.gameserver.service.host.APIResponse;

import java.util.Map;

/**
 * Created by Nick on 31/01/2015.
 *
 * Response from API when updating a server
 */
public class UpdateServer {
    private boolean success;
    private Map<String, String[]> errors;

    /**
     * Whether or not the create/update was successful
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * A map of all errors
     * @return
     */
    public Map<String, String[]> getErrors() {
        return errors;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrors(Map<String, String[]> errors) {
        this.errors = errors;
    }
}
