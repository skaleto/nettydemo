package im.codec;

import im.constants.ProtocolConst;
import im.proto.IMMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class IMEncoder extends MessageToByteEncoder<IMMsg.ProtoMsg.Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IMMsg.ProtoMsg.Message msg, ByteBuf out) throws Exception {
        out.writeShort(ProtocolConst.MAGIC);
        out.writeShort(ProtocolConst.PROTOCOL_VER);

        byte[] data = msg.toByteArray();

        out.writeInt(data.length);
        out.writeBytes(data);

    }
}
