package pers.daedalus.common.trans;

import lombok.Data;

/**
 * 封装 rpc 响应
 */
@Data
public class RpcResponse {
    private String requestId;
    private Throwable error;
    private Object result;

    public boolean isError() {
        return error != null;
    }
}
