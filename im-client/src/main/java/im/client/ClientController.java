package im.client;


import ch.qos.logback.core.net.server.Client;
import im.client.nettyclient.NettyClient;
import im.client.session.ClientSession;
import im.proto.IMMsg;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Future;

/**
 * 控制客户端进行连接、登录、发送消息、登出的入口
 */
@Slf4j
@Service
public class ClientController {

    @Resource
    private NettyClient nettyClient;

    private ClientSession session;

    /**
     * 连接并登录
     *
     * @throws Exception
     */
    public void connectAndLogin() throws Exception {
        GenericFutureListener<ChannelFuture> connectListener = (ChannelFuture f) -> {
            //假如连接成功，需要创建一个客户端会话，主要用来存放通信的channel
            if (f.isSuccess()) {
                log.info("connect success");

                session = new ClientSession("-1", f.channel());

                ChannelFuture writeAndFlushFuture = f.channel().writeAndFlush(buildLoginReq());
                writeAndFlushFuture.addListener((future) -> {
                    // 回调
                    if (future.isSuccess()) {
                        log.info("send success");
                    } else {
                        log.info("send fail");
                    }

                });
            }
        };
        nettyClient.setConnectListener(connectListener);
        nettyClient.connect();
    }


    /**
     *
     */
    public void sendMessage() {

    }

    public void logout() {

    }


    private IMMsg.ProtoMsg.Message buildLoginReq() {
        IMMsg.ProtoMsg.Message.Builder message = IMMsg.ProtoMsg.Message.newBuilder()
                .setType(IMMsg.ProtoMsg.MsgType.LOGIN_REQUEST);

        IMMsg.ProtoMsg.LoginRequest request = IMMsg.ProtoMsg.LoginRequest.newBuilder()
                .setDeviceId("device1")
                .setAppVersion("1.0")
                .setPlatform("Android")
                .setUid("12345")
                .build();
        message.setLoginRequest(request);
        return message.build();
    }
}
