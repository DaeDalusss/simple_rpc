package pers.daedalus.common.tools;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * rpc 编码器
 */
public class RpcEncoder extends MessageToByteEncoder {

    private Class<?> genericClass;

    /**
     * 构造函数
     * @param genericClass 传入向反序列化的class
     */
    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    /**
     * 序列化编码
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out)
            throws Exception {
        //序列化
        if (genericClass.isInstance(in)) {
            byte[] data = SerializationUtil.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
