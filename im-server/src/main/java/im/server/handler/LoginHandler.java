package im.server.handler;

import im.proto.IMMsg;
import im.server.session.ServerSession;
import im.server.session.ServerSessionManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
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
        if (type == IMMsg.ProtoMsg.MsgType.LOGIN_REQUEST) {
            ServerSessionManager.addSession(message.getSessionId(), new ServerSession(ctx.channel()));
            //
            ctx.writeAndFlush(buildLoginResponse(0, "success", true));
        }


    }

    private IMMsg.ProtoMsg.Message buildLoginResponse(int code, String desc, boolean result) {
        IMMsg.ProtoMsg.Message.Builder message = IMMsg.ProtoMsg.Message.newBuilder()
                .setType(IMMsg.ProtoMsg.MsgType.LOGIN_RESPONSE);

        IMMsg.ProtoMsg.LoginResponse response = IMMsg.ProtoMsg.LoginResponse.newBuilder()
                .setCode(code)
                .setDesc(desc)
                .setResult(result)
                .build();

        return message.setLoginResponse(response).build();
    }

}