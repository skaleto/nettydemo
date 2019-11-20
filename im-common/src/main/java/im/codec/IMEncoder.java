package im.codec;

import im.Constants;
import im.proto.IMMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Service;

public class IMEncoder extends MessageToByteEncoder<IMMsg.ProtoMsg.Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IMMsg.ProtoMsg.Message msg, ByteBuf out) throws Exception {
        out.writeShort(Constants.MAGIC);
        out.writeShort(Constants.PROTOCOL_VER);

        byte[] data = msg.toByteArray();

        out.writeInt(data.length);
        out.writeBytes(data);

    }
}
