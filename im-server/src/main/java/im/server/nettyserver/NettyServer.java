package im.server.nettyserver;

import im.codec.IMDecoder;
import im.codec.IMEncoder;
import im.server.handler.LoginHandler;
import im.server.handler.MsgHandler;
import im.server.handler.ServerExceptionHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import javafx.util.converter.BooleanStringConverter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : ybyao
 * @Create : 2019-11-19 10:33
 */
@Service
public class NettyServer {

    @Resource
    private LoginHandler loginHandler;

    @Resource
    private MsgHandler msgHandler;

    @Resource
    private ServerExceptionHandler serverExceptionHandler;

    public NettyServer() {
    }

    public void startServer() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR,
                PooledByteBufAllocator.DEFAULT);
        bootstrap
                .group(bossGroup, workerGroup)
                //为工作线程的工作流添加解码编码器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new IMDecoder());
                        socketChannel.pipeline().addLast(new IMEncoder());
                        socketChannel.pipeline().addLast(loginHandler);
                        socketChannel.pipeline().addLast(msgHandler);
                        socketChannel.pipeline().addLast(serverExceptionHandler);
                    }
                });

        bootstrap.bind(9999);

    }

}
