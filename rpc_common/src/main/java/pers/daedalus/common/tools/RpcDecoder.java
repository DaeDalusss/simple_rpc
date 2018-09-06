package pers.daedalus.common.tools;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * rpc 解码器
 */
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    /**
     * 构造器
     * @param genericClass 反序列化的class
     */
    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    /**
     * 解码方法
     * @param ctx 接口
     * @param in 抽象类
     * @param out 接口
     * @throws Exception
     */
    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        int dataLength = in.readInt(); //数据长度
        if (dataLength < 0) { //没有数据时关闭channel 处理的上下文
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex(); //校正数据长度和读取标记
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);//从buffer 中读取数据到 byte数组
        //将data 转换成object -- 反序列化
        Object obj = SerializationUtil.deserialize(data, genericClass);
        out.add(obj);
    }
}
