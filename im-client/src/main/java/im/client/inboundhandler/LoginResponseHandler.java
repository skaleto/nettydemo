package im.client.inboundhandler;

import im.client.helper.CommandHelper;
import im.client.nettyclient.NettyClient;
import im.client.session.ClientSession;
import im.constants.BizConst;
import im.constants.ProtocolConst;
import im.proto.IMMsg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * 登录请求的返回消息处理器
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private NettyClient nettyClient;

    @Resource
    private CommandHelper commandHelper;

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
            //从channel中取出当前通道中的会话对象，用于设置会话id
            ClientSession session = ctx.channel().attr(ClientSession.SESSION_KEY).get();
            session.setSessionId(message.getSessionId());
            log.info(session.toString());

            if (ProtocolConst.WRONG_SESSION.equals(session.getSessionId())) {
                System.out.println("登录失败");
            }else{
                System.out.println("登录成功");
                //登录成功后，这条pipeline上不再需要loginResponse了
                ctx.pipeline().remove(LoginResponseHandler.class);
            }

            //唤醒控制线程
            commandHelper.notifyCommandThread();

        }

    }
}
