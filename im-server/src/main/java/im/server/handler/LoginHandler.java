package im.server.handler;


import im.bean.UserInfo;
import im.constants.BizConst;
import im.constants.ProtocolConst;
import im.proto.IMMsg;
import im.server.session.ServerSession;
import im.server.session.ServerSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


/**
 * @author : ybyao
 * @Create : 2019-11-19 10:46
 */

@Slf4j
@Service
@ChannelHandler.Sharable
public class LoginHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof IMMsg.ProtoMsg.Message)) {
            log.error("msg type error");
            super.channelRead(ctx, msg);
            return;
        }

        IMMsg.ProtoMsg.Message message = (IMMsg.ProtoMsg.Message) msg;
        IMMsg.ProtoMsg.MsgType type = message.getType();
        if (type != IMMsg.ProtoMsg.MsgType.LOGIN_REQUEST) {
            super.channelRead(ctx, msg);
            return;
        }

        //校验登录
        IMMsg.ProtoMsg.LoginRequest req = message.getLoginRequest();
        UserInfo userInfo = new UserInfo(req.getUid(), req.getToken(), req.getDeviceId(), req.getAppVersion(), req.getPlatform());

        if (!authCheck(userInfo)||ServerSessionManager.getSessionList(req.getUid()) != null) {
            log.warn("该用户验证失败或已登录！");
            //发送登录失败的报文
            ctx.writeAndFlush(buildLoginResponse(message.getSequence(), "-1", ProtocolConst.LOGIN_FAIL_CODE, ProtocolConst.LOGIN_FAIL_MSG, false));
            return;
        }

        ServerSession serverSession = new ServerSession(ctx.channel());
        ServerSessionManager.addSession(userInfo.getUid(), serverSession);
        ctx.writeAndFlush(buildLoginResponse(message.getSequence(), serverSession.getSessionId(), ProtocolConst.SUCCESS_CODE, ProtocolConst.SUCCESS_MSG, true));

        //通道建立成功后，这条线上不再需要登录处理器了
        ctx.pipeline().remove(LoginHandler.class);

    }

    private boolean authCheck(UserInfo userInfo) {
        //这里密码验证从简
        return userInfo != null && StringUtils.equals(userInfo.getToken(), BizConst.token);
    }


    //TODO 这里的sequence起了什么作用？
    private IMMsg.ProtoMsg.Message buildLoginResponse(long sequence, String sessionId, int code, String desc, boolean result) {
        IMMsg.ProtoMsg.Message.Builder message = IMMsg.ProtoMsg.Message.newBuilder()
                .setType(IMMsg.ProtoMsg.MsgType.LOGIN_RESPONSE)
                .setSequence(sequence)
                .setSessionId(sessionId);

        IMMsg.ProtoMsg.LoginResponse response = IMMsg.ProtoMsg.LoginResponse.newBuilder()
                .setCode(code)
                .setDesc(desc)
                .setResult(result)
                .build();

        return message.setLoginResponse(response).build();
    }

}
