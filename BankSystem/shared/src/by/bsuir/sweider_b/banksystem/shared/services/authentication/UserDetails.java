package by.bsuir.sweider_b.banksystem.shared.services.authentication;

import java.io.Serializable;

/**
 * Created by sweid on 06.12.2015.
 */
public class UserDetails implements Serializable{
    private final String nameToDisplay;

    public UserDetails(String nameToDisplay) {
        this.nameToDisplay = nameToDisplay;
    }

    public String getNameToDisplay() {
        return nameToDisplay;
    }
}
