package by.bsuir.sweider_b.services.authentication;

/**
 * Created by sweid on 06.12.2015.
 */
public class UserDetails {
    private final String nameToDisplay;

    public UserDetails(String nameToDisplay) {
        this.nameToDisplay = nameToDisplay;
    }

    public String getNameToDisplay() {
        return nameToDisplay;
    }
}
