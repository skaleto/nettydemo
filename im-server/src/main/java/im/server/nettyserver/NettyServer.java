package im.server.nettyserver;

import im.codec.IMDecoder;
import im.codec.IMEncoder;
import im.server.handler.LoginHandler;
import im.server.handler.MsgHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : ybyao
 * @Create : 2019-11-19 10:33
 */
@Service
public class NettyServer {

    @Resource
    private IMDecoder imDecoder;

    @Resource
    private IMEncoder imEncoder;

    @Resource
    private LoginHandler loginHandler;

    @Resource
    private MsgHandler msgHandler;

    public NettyServer() {
    }

    public void startServer() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
                .group(bossGroup, workerGroup)
                .channel(ServerSocketChannel.class)
                //为工作线程的工作流添加解码编码器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(imDecoder);
                        socketChannel.pipeline().addLast(imEncoder);
                        socketChannel.pipeline().addLast(loginHandler);
                        socketChannel.pipeline().addLast(msgHandler);
                    }
                });

        bootstrap.bind(9999);

    }

}
