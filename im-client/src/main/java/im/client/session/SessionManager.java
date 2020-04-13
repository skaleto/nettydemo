package im.client.session;

import im.bean.UserInfo;
import im.constants.BizConst;
import io.netty.channel.ChannelFuture;
import org.springframework.stereotype.Service;

/**
 * @author : ybyao
 * @Create : 2020-01-08 17:58
 */

@Service
public class SessionManager {

    private ClientSession session;

    public void createSession(String userId, String token, ChannelFuture f) {
        UserInfo user = new UserInfo(userId, token, "ALT-100", "1.0.0000", "Android");
        session = new ClientSession("-1", user, f.channel());
    }

    public ClientSession getSession() {
        return session;
    }
}
