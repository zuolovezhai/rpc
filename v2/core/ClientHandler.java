package rpc.v2.core;

import rpc.v2.protocol.MessageProtocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class ClientHandler {
    private RpcClient rpcClient;
    private MessageProtocol messageProtocol;

    public ClientHandler(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public void setMessageProtocol(MessageProtocol messageProtocol) {
        this.messageProtocol = messageProtocol;
    }

    public <T> RpcResponse<T> invoke(RpcRequest request, InetSocketAddress address) {
        RpcResponse<T> response = new RpcResponse();
        Socket socket = getSocketInstance(address);
        if (socket == null) {
            response.setError(true);
            response.setErrorMessage("套接字连接失败");
            return response;
        }
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            messageProtocol.clientToRequest(request, outputStream);
            response = messageProtocol.clientGetResponse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    private Socket getSocketInstance(InetSocketAddress address) {
        try {
            return new Socket(address.getHostString(), address.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
