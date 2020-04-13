package im.server.session;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : ybyao
 * @Create : 2019-11-19 11:05
 */
public class ServerSessionManager {

    //每个user的session集合
    private static ConcurrentHashMap<String, List<String>> userSessionMap = new ConcurrentHashMap<>();
    //sessionId的映射表
    private static ConcurrentHashMap<String, ServerSession> sessionMap = new ConcurrentHashMap<>();

    public static void addSession(String userId, ServerSession session) {
        sessionMap.put(session.getSessionId(), session);
        List<String> list = userSessionMap.get(userId);
        if (list == null) {
            list = new ArrayList<>();
            list.add(session.getSessionId());
            userSessionMap.put(userId, list);
        } else {
            list.add(session.getSessionId());
        }
    }

    public static void removeSession(String userId, String sessionId) {
        List<String> list = userSessionMap.get(userId);
        if (!CollectionUtils.isEmpty(list)) {
            list.remove(sessionId);
        }
        sessionMap.remove(sessionId);
    }

    public static List<ServerSession> getSessionList(String userId) {
        List<String> list = userSessionMap.get(userId);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<ServerSession> sessionList = new ArrayList<>();
        for (String s : list) {
            sessionList.add(sessionMap.get(s));
        }
        return sessionList;
    }
}
