package im.client.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Data;

@Data
public class ClientSession {

    public static final AttributeKey<ClientSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    /**
     * 该会话的id
     */
    private String sessionId;

    /**
     * 该会话的通道
     */
    private Channel channel;

    public ClientSession(String sessionId, Channel channel) {
        this.sessionId = sessionId;
        this.channel = channel;
        //将当前的session对象绑定到channel上
        channel.attr(SESSION_KEY).set(this);
    }
}
