package org.temqua.sessionService;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Артем on 27.09.2017.
 */
public class SessionService {
    private static BlockingQueue<HttpSession> sessionsQueue = new ArrayBlockingQueue<>(10);

    public static BlockingQueue<HttpSession> getSessionsQueue() {
        return sessionsQueue;
    }
}
