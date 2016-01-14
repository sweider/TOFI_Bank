package by.bsuir.sweider_b.banksystem.server.services.sessions;

/**
 * Created by sweid on 06.12.2015.
 */
public class SessionState {
    private final int timer;
    private volatile boolean isExpired;
    private int minutesLeft;

    public SessionState(int minutesUntilExpiration){
        this.timer = minutesUntilExpiration;
        this.minutesLeft = minutesUntilExpiration;
        this.isExpired = false;
    }

    public synchronized boolean isExpired() {
        return isExpired;
    }

    public synchronized void dropTimerToStart(){
        this.minutesLeft = this.timer;
    }

    public synchronized void decreaseLeftTimeByMinutes(int value){
        this.minutesLeft -= value;
        if(this.minutesLeft <= 0) { this.isExpired = true; }
    }
}
