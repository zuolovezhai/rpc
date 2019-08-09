package rpc.v2.protocol;

import rpc.v2.core.RpcRequest;
import rpc.v2.core.RpcResponse;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public interface MessageProtocol {
    /**
     * 服务端解析从网络传输过来的数据，转变成request
     * @param inputStream
     * @return
     */
    RpcRequest serviceToRequest(InputStream inputStream);

    /**
     * 服务端把计算机的结果包装好，通过outputStream返回给客户端
     * @param response
     * @param outputStream
     * @param <T>
     */
    <T> void serviceGetResponse(RpcResponse<T> response, OutputStream outputStream);

    /**
     * 客户端把请求拼装好，通过outputStream发送到f服务端
     * @param request
     * @param outputStream
     */
    void clientToRequest(RpcRequest request, OutputStream outputStream);

    /**
     * 客户端接收到服务器响应的结果，转变成response
     * @param inputStream
     * @param <T>
     * @return
     */
    <T> RpcResponse<T> clientGetResponse(InputStream inputStream);

}
