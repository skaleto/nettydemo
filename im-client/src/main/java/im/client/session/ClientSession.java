package im.client.session;

import im.bean.UserInfo;
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

    /**
     * 用户信息
     */
    private UserInfo userInfo;



    public ClientSession(String sessionId, UserInfo userInfo, Channel channel) {
        this.sessionId = sessionId;
        this.userInfo = userInfo;
        this.channel = channel;
        //将当前的session对象绑定到channel上
        channel.attr(SESSION_KEY).set(this);
    }


}
