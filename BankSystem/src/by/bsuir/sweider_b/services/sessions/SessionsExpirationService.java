package by.bsuir.sweider_b.services.sessions;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by sweid on 06.12.2015.
 */
@Service
public class SessionsExpirationService {
    private final static int DEFAULT_EXPIRATION_TIME = 15;
    private final static int DEFAULT_CHECK_FREQ = 1;

    private final ExecutorService executor;
    private final HashMap<String, SessionState> storage;
    private final ArrayList<Consumer<Stream<String>>> expirationListeners;
    private final Cleaner cleaner;

    public SessionsExpirationService(){
        this.executor = Executors.newCachedThreadPool();
        this.storage = new HashMap<>();
        this.expirationListeners = new ArrayList<>();
        this.cleaner = new Cleaner(this);
    }

    public void putSession(String sessionId){
        SessionState existingSession = this.storage.get(sessionId);
        if(existingSession == null) {
            final SessionState sessionState = new SessionState(DEFAULT_EXPIRATION_TIME);
            this.storage.put(sessionId, sessionState);
            executor.submit(() -> {
                try {
                    while (!sessionState.isExpired()) {
                        sessionState.decreaseLeftTimeByMinutes(DEFAULT_CHECK_FREQ);
                        Thread.sleep(DEFAULT_CHECK_FREQ * 60 * 1000);
                    }
                } catch (InterruptedException ex) {
                }
            });
        }
        else{
            existingSession.dropTimerToStart();
        }
    }

    /**
     * Проверяет наличие сессии, и если есть -- сбрасывает таймер
     * @param sessionId
     * @return
     */
    public boolean checkSession(String sessionId){
        Optional<SessionState> state = Optional.ofNullable(this.storage.get(sessionId));
        state.ifPresent(SessionState::dropTimerToStart);
        return state.isPresent();
    }

    protected void addSessionsExpiredListener(Consumer<Stream<String>> listener){
        this.expirationListeners.add(listener);
    }

    @PostConstruct
    private void init(){
        executor.submit(this.cleaner);
    }

    private void clean(){
        Stream<String> expiredStream = this.storage.entrySet().stream().filter(entry -> entry.getValue().isExpired()).map(Map.Entry::getKey);
        expiredStream.forEach(this.storage::remove);
        this.expirationListeners.forEach( listener -> listener.accept(expiredStream));
    }

    private class Cleaner implements Runnable{
        private volatile boolean isActive;
        private final SessionsExpirationService service;


        private Cleaner(SessionsExpirationService service) {
            this.service = service;
        }

        public void run(){
            try {
                while (isActive) {
                    Thread.sleep((DEFAULT_EXPIRATION_TIME + 1) * 60 * 1000);
                    this.service.clean();
                }
            }
            catch(InterruptedException ex) { }
        }

        private void stop(){
            this.isActive = false;
        }


    }
}
