package basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyDiscardServer {
    private EventLoopGroup bossLoopGroup;
    private EventLoopGroup workerLoopGroup;
    private ChannelFuture channelFuture;

    public static void main(String[] args) throws InterruptedException {
        new NettyDiscardServer().runServer();
    }

    public void runServer() throws InterruptedException {
        bossLoopGroup = new NioEventLoopGroup(1);
        workerLoopGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossLoopGroup, workerLoopGroup);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(9999);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        //这里是为子通道装配流水线的操作
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            //有连接到达的时候会创建一个通道的子通道
            protected void initChannel(SocketChannel ch) throws Exception {
                //流水线管理子通道中的Handler业务处理器
                //向子通道流水线添加一个Handler业务处理器
                ch.pipeline().addLast(new NettyDiscardHandler());
            }
        });

        channelFuture = bootstrap.bind().sync();

    }

    public void shutDownServer() throws InterruptedException {
        ChannelFuture close = channelFuture.channel().closeFuture();
        close.sync();

        workerLoopGroup.shutdownGracefully();
        bossLoopGroup.shutdownGracefully();

    }
}
