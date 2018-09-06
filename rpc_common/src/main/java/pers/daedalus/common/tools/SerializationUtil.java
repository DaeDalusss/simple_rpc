package pers.daedalus.common.tools;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化与反序列化工具
 * 依赖的jar ：
 *      Protostuff -- Protostuff是一个开源的、基于Java语言的序列化库，它内建支持向前向后兼容（模式演进）和验证功能。
 *      Objenesis  -- Objenesis是一个轻量级的Java库，作用是绕过构造器创建一个实例。
 *
 *
 *
 */
public class SerializationUtil {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();//缓存 享元模式
    private static Objenesis objenesis = new ObjenesisStd(true);

    private SerializationUtil(){}

    private static <T> Schema<T> getSchema(Class<T> cls){
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null){
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }

    /**
     * 序列化 （对象 -> 字节数组）
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);//序列化
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化（字节数组 -> 对象）
     * @param data
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
        	/*
        	 * 如果一个类没有参数为空的构造方法时候，那么你直接调用newInstance方法试图得到一个实例对象的时候是会抛出异常的
        	 * 通过ObjenesisStd可以完美的避开这个问题
        	 */
            T message = (T) objenesis.newInstance(cls);//实例化
            Schema<T> schema = getSchema(cls);//获取类的schema
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
