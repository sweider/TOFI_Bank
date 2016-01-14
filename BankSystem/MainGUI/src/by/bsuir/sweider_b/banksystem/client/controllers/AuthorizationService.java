package by.bsuir.sweider_b.banksystem.client.controllers;

import by.bsuir.sweider_b.banksystem.shared.services.authentication.AuthenticationResult;
import by.bsuir.sweider_b.banksystem.shared.services.authentication.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Created by sweid on 14.01.2016.
 */
@Service
public class AuthorizationService {
    private String sessionId;
    private UserDetails currentUserDetails;

    public void processAuthenticationResult(AuthenticationResult result){
        this.currentUserDetails = result.getUserDetails();
        this.sessionId = result.getSessionId();
    }

    public String getUserName(){
        return this.currentUserDetails.getNameToDisplay();
    }

}
