package im.client.nettyclient;


import im.Constants;
import im.client.inboundhandler.ChatMsgHandler;
import im.client.inboundhandler.LoginResponseHandler;
import im.codec.IMDecoder;
import im.codec.IMEncoder;
import im.proto.IMMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@Slf4j
@Service
public class NettyClient {

    @Resource
    private ChatMsgHandler chatMsgHandler;

    @Resource
    private LoginResponseHandler loginResponseHandler;

    public NettyClient() {

    }

    public void connect() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.remoteAddress(new InetSocketAddress(9999));
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new IMEncoder());
                ch.pipeline().addLast(new IMDecoder());
                ch.pipeline().addLast(chatMsgHandler);
                ch.pipeline().addLast(loginResponseHandler);

            }
        });
        ChannelFuture connectFuture = bootstrap.connect().addListener((ChannelFuture f) -> {
            if (f.isSuccess()) {
                log.info("connect success");

                ChannelFuture future = f.channel().writeAndFlush(buildLoginReq());
                future.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future)
                            throws Exception {
                        // 回调
                        if (future.isSuccess()) {
                            log.info("send success");
                        } else {
                            log.info("send fail");
                        }
                    }

                });
            }
        });
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
