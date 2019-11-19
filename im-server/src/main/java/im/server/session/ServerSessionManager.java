package im.server.session;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : ybyao
 * @Create : 2019-11-19 11:05
 */
public class ServerSessionManager {

    private static ConcurrentHashMap<String, ServerSession> sessionMap = new ConcurrentHashMap<>();

    public static void addSession(String sessionId, ServerSession session) {
        sessionMap.put(sessionId, session);
    }

    public static void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }
}
