import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InboundHandlerTest {

    @Test
    public void test() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ChannelInitializer<EmbeddedChannel>() {
            protected void initChannel(EmbeddedChannel ch) throws Exception {
                ch.pipeline().addLast(new Outbound1());
                ch.pipeline().addLast(new Outbound2());
                ch.pipeline().addLast(new Outbound3());
            }
        });

        //创建buffer的最快方法，内部与UnpooledByteBufAllocator.DEFAULT.heapBuffer()一致
        ByteBuf buf = Unpooled.buffer();
        //创建一个初始容量10，最大扩容容量100的buffer
        ByteBuf buf2= ByteBufAllocator.DEFAULT.buffer(10,100);
        //创建默认初始容量为256，最大扩容容量为Integer.MAX_VALUE的buffer
        ByteBuf buf3=ByteBufAllocator.DEFAULT.buffer();
        //创建基于java堆结构的非池化buffer
        ByteBuf buf4= UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        //创建基于操作系统直接内存的池化buffer
        ByteBuf buf5= PooledByteBufAllocator.DEFAULT.directBuffer();
        buf.writeInt(1);

        embeddedChannel.writeInbound(buf);
        embeddedChannel.flush();

        embeddedChannel.writeInbound(buf);
        embeddedChannel.flush();

        embeddedChannel.writeOutbound(buf);

        embeddedChannel.close();

    }

    static class Outbound1 extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("Outbound1 write");
            super.write(ctx, msg, promise);
        }
    }

    static class Outbound2 extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("Outbound2 write");
//            super.write(ctx, msg, promise);
        }
    }

    static class Outbound3 extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.out.println("Outbound3 write");
            super.write(ctx, msg, promise);
        }
    }
}