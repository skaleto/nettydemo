package im.client.nettyclient;


import im.client.inboundhandler.ChatMsgHandler;
import im.client.inboundhandler.ClientExceptionHandler;
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
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@Slf4j
@Data
@Service
public class NettyClient {

    @Resource
    private ChatMsgHandler chatMsgHandler;

    @Resource
    private LoginResponseHandler loginResponseHandler;

    @Resource
    private ClientExceptionHandler clientExceptionHandler;

    GenericFutureListener<ChannelFuture> connectListener;

    public NettyClient() {

    }

    public void connect() throws Exception {
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
                ch.pipeline().addLast(clientExceptionHandler);

            }
        });
        if (connectListener==null){
            throw new Exception("监听器未初始化");
        }
        ChannelFuture connectFuture = bootstrap.connect().addListener(connectListener);
    }


}
