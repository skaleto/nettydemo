package im.client.inboundhandler;

import im.proto.IMMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ChannelHandler.Sharable
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof IMMsg.ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        IMMsg.ProtoMsg.Message message = (IMMsg.ProtoMsg.Message) msg;
        if (message.getType() == IMMsg.ProtoMsg.MsgType.LOGIN_RESPONSE) {
            log.info(message.getLoginResponse().getDesc());
        }

    }
}
