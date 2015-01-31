package com.nicktoony.service.host.APIResponse;

/**
 * Created by Nick on 31/01/2015.
 *
 * Response from API when creating a server
 */
public class CreateServer extends UpdateServer {
    private String password;
    private String id;

    /**
     * Returns the this server's password, which is used to update the server list again
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * The ID number of this server
     *
     * @return
     */
    public String getId() {
        return id;
    }
}
