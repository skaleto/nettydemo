package im.client;


import ch.qos.logback.core.net.server.Client;
import im.bean.UserInfo;
import im.client.helper.CommandHelper;
import im.client.nettyclient.NettyClient;
import im.client.session.ClientSession;
import im.client.session.SessionManager;
import im.constants.BizConst;
import im.proto.IMMsg;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * 控制客户端进行连接、登录、发送消息、登出的入口
 */
@Slf4j
@Service
public class ClientController {

    @Resource
    private NettyClient nettyClient;

    @Resource
    private SessionManager sessionManager;


    /**
     * 连接并登录，监听连接成功事件，成功则触发登录
     *
     * @throws Exception
     */
    public void connectAndLogin(String userId, String token) throws Exception {
        GenericFutureListener<ChannelFuture> connectListener = (ChannelFuture f) -> {
            //假如连接成功，需要创建一个客户端会话，主要用来存放通信的channel
            if (f.isSuccess()) {
                log.info("connect success");
                //创建会话
                sessionManager.createSession(userId, token, f);
                ChannelFuture writeAndFlushFuture = f.channel()
                        .writeAndFlush(buildLoginReq(sessionManager.getSession().getUserInfo()));
                writeAndFlushFuture.addListener((future) -> {
                    // 回调
                    if (future.isSuccess()) {
                        log.info("login msg send success");
                    } else {
                        log.info("log msg send fail");
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


    private IMMsg.ProtoMsg.Message buildLoginReq(UserInfo user) {
        IMMsg.ProtoMsg.Message.Builder message = IMMsg.ProtoMsg.Message.newBuilder()
                .setType(IMMsg.ProtoMsg.MsgType.LOGIN_REQUEST);

        IMMsg.ProtoMsg.LoginRequest request = IMMsg.ProtoMsg.LoginRequest.newBuilder()
                .setDeviceId(user.getDeviceId())
                .setAppVersion(user.getAppVersion())
                .setPlatform(user.getPlatform())
                .setUid(user.getUid())
                .setToken(user.getToken())
                .build();
        message.setLoginRequest(request);
        return message.build();
    }
}
