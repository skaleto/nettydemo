//package im.proto;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//public class ProtoTest {
//
//    public static MsgProtos.Msg buildMsg() {
//        MsgProtos.Msg.Builder builder = MsgProtos.Msg.newBuilder();
//        builder.setId(1);
//        builder.setContent("this is 1");
//        MsgProtos.Msg msg = builder.build();
//        return msg;
//    }
//
//    public static void testSerialization() throws IOException {
//        MsgProtos.Msg msg = buildMsg();
//
//        byte[] data = msg.toByteArray();
//        msg.writeTo(new ByteArrayOutputStream());
//        msg.writeDelimitedTo(new ByteArrayOutputStream());
//
//        MsgProtos.Msg.parseFrom(data);
////        MsgProtos.Msg.parseFrom(inputstream);
////        MsgProtos.Msg.parseDelimitedFrom(inputstream);
//
////        ProtobufVarint32LengthFieldPrepender
////        ProtobufVarint32FrameDecoder
//    }
//
//}
