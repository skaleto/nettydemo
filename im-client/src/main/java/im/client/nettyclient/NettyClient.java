package im.client.nettyclient;


import im.client.inboundhandler.ChatMsgHandler;
import im.client.inboundhandler.LoginResponseHandler;
import im.codec.IMDecoder;
import im.codec.IMEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@Slf4j
@Service
public class NettyClient {

    @Resource
    private IMDecoder imDecoder;

    @Resource
    private IMEncoder imEncoder;

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
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(imDecoder);
                ch.pipeline().addLast(imEncoder);
                ch.pipeline().addLast(chatMsgHandler);
                ch.pipeline().addLast(loginResponseHandler);

            }
        });
        ChannelFuture connectFuture = bootstrap.connect().addListener(future -> {
            if(future.isSuccess()){
                log.info("connect success");
            }
        });
    }
}
