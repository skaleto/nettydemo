package im.codec;

import im.Constants;
import im.proto.IMMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IMDecoder extends ByteToMessageDecoder {


    /**
     * 协议按照 |2字节魔数|2字节协议版本|4字节数据长度|实际数据
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //标记当前指针位置，便于出现问题后快速恢复
        in.markReaderIndex();

        if (in.readableBytes() < 8) {
            //数据不够时不属于异常，只需要再次等待
            return;
        }
        //读取并校验魔数
        if (in.readShort() != Constants.MAGIC) {
            throw new Exception("MAGIC ERROR");
        }
        //读取并校验协议版本
        if (in.readShort() != Constants.PROTOCOL_VER) {
            throw new Exception("PROTOCOL VERSION ERROR");
        }
        //读取数据长度
        int contentLength = in.readInt();
        if (contentLength < 0) {
            //数据异常，关闭连接
            ctx.close();
        }
        if (in.readableBytes() < contentLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] data;
        //hasArray用来判断是否使用堆内存
        if (in.hasArray()) {
            //如果有array存放则说明使用的是堆内存，array中存放的即真实数据
            //这里做了一次slice浅复制
            ByteBuf buf = in.slice();
            data = buf.array();
        } else {
            data = new byte[contentLength];
            in.readBytes(data);
        }

        //反序列化转换成proto POJO对象
        IMMsg.ProtoMsg msg = IMMsg.ProtoMsg.parseFrom(data);
        if (msg != null) {
            out.add(msg);
        }

    }
}
