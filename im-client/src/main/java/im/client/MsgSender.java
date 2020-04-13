package im.client;

import im.bean.UserInfo;
import im.client.session.SessionManager;
import im.proto.IMMsg;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : ybyao
 * @Create : 2020-01-08 18:58
 */
@Slf4j
@Service
public class MsgSender {

    @Resource
    private SessionManager sessionManager;

    public void sendMsg(String msg) {
        if (sessionManager.getSession() == null) {
            return;
        }
        String[] array = msg.split(":");
        ChannelFuture future = sessionManager.getSession().getChannel()
                .writeAndFlush(buildMsgReq(array[0], array[1]));
        future.addListener((f) -> {
            if (f.isSuccess()) {
                log.info("send msg success");
            } else {
                log.error("send msg error");
            }
        });

    }

    private IMMsg.ProtoMsg.Message buildMsgReq(String toClient, String msg) {
        IMMsg.ProtoMsg.Message.Builder message = IMMsg.ProtoMsg.Message.newBuilder()
                .setType(IMMsg.ProtoMsg.MsgType.MESSAGE_REQUEST);

        IMMsg.ProtoMsg.MessageRequest request = IMMsg.ProtoMsg.MessageRequest.newBuilder()
                .setContent(msg)
                .setFromClient("127.0.0.1")
                .setToClient(toClient)
                .setMsgId(1)
                .build();
        message.setMessageRequest(request);
        return message.build();
    }

}
