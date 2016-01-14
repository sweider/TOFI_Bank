package by.bsuir.sweider_b.banksystem.shared.services.authentication;

import java.io.Serializable;

/**
 * Created by sweid on 06.12.2015.
 */
public class AuthenticationResult implements Serializable{
    private final String sessionId;

    private final UserDetails userDetails;

    public AuthenticationResult(String sessionId, UserDetails userDetails) {
        this.sessionId = sessionId;
        this.userDetails = userDetails;
    }

    public String getSessionId() {
        return sessionId;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }
}
