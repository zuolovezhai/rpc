package rpc.v2.protocol;

import rpc.v2.core.RpcRequest;
import rpc.v2.core.RpcResponse;
import rpc.v2.serialize.JavaInnerSerialize;
import rpc.v2.serialize.SerializeProtocol;

import java.io.*;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class DefaultMessageProtocol implements MessageProtocol {

    private SerializeProtocol serializeProtocol;

    public DefaultMessageProtocol() {
        this.serializeProtocol = new JavaInnerSerialize();
    }

    public void setSerializeProtocol(SerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    @Override
    public RpcRequest serviceToRequest(InputStream inputStream) {
        try {
            byte[] bytes = readBytes(inputStream);
            System.out.println("[2]服务器反序列化出obj length: " + bytes.length);
            return serializeProtocol.deserialize(RpcRequest.class, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> void serviceGetResponse(RpcResponse<T> response, OutputStream outputStream) {
        try {
            byte[] bytes = serializeProtocol.serialize(RpcResponse.class, response);
            System.out.println("[3]服务端序列化出bytes length: " + bytes.length);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clientToRequest(RpcRequest request, OutputStream outputStream) {
        try {
            byte[] bytes = serializeProtocol.serialize(RpcRequest.class, request);
            System.out.println("[1]客户端序列化出bytes length: " + bytes.length);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> RpcResponse<T> clientGetResponse(InputStream inputStream) {
        try {
            byte[] bytes = readBytes(inputStream);
            System.out.println("[4]客户端反序列化出bytes length: " + bytes.length);
            return serializeProtocol.deserialize(RpcResponse.class, bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new RuntimeException("input为空");
        }
        return fun2(inputStream);
    }

    /**
     * 此方法最多写1024个字节
     * @param inputStream
     * @return byte[]
     * @throws IOException
     */
    private byte[] fun1(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1025];
        inputStream.read(bytes);
        return bytes;
    }

    private byte[] fun2(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufSize = 1024;
        while (true) {
            byte[] bytes = new byte[1024];
            int count = inputStream.read(bytes);
            byteArrayOutputStream.write(bytes, 0, count);
            if (count < bufSize) {
                break;
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 有问题的fun3，调用之后会阻塞在read，可通过jstack查看相关信息
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] fun3(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufeSize = 1024;
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, bufeSize)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
            buff = new byte[1024];
        }
        return byteArrayOutputStream.toByteArray();
    }
}
