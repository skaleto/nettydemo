package im.server.session;

import io.netty.channel.Channel;
import lombok.Data;


/**
 * @author : ybyao
 * @Create : 2019-11-19 11:04
 */
@Data
public class ServerSession {

    private Channel channel;

    public ServerSession(Channel channel) {
        this.channel = channel;
    }

}
