package pers.daedalus.common.trans;

import lombok.Data;

/**
 * 封装 rpc 请求对象
 *      封装发送的object的反射属性
 */
@Data
public class RpcRequest {

    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;



}
