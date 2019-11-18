package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.handler.codec.*;

import java.util.List;

public class MyStringDecoder extends ReplayingDecoder<MyStringDecoder.Status> {

    enum Status {
        READ_LENGTH,
        GET_STR
    }

    private int length;

    public MyStringDecoder() {
        super(Status.READ_LENGTH);
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (state()) {
            case READ_LENGTH:
                length = in.readInt();
                checkpoint(Status.GET_STR);
                break;
            case GET_STR:
                byte[] bytes = new byte[length];
                in.readBytes(bytes);
                out.add(new String(bytes, "UTF-8"));
                checkpoint(Status.READ_LENGTH);
                break;
            default:
                break;
        }
    }
}
