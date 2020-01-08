package im.server.session;

import im.bean.UserInfo;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.UUID;


/**
 * @author : ybyao
 * @Create : 2019-11-19 11:04
 */
@Data
public class ServerSession {

    /**
     * 会话绑定的消息channel
     */
    private Channel channel;
    /**
     * 会话id，随机生成
     */
    private String sessionId;

    private UserInfo user;

    public ServerSession(Channel channel) {
        this.channel = channel;
        this.sessionId = generateSessionId();
    }

    private static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

}
