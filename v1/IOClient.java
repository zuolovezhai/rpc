package rpc.v1;

import java.io.*;
import java.net.Socket;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class IOClient {
    private String ip;
    private int port;

    public IOClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Object invoke(MethodParameter methodParameter) {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(outputStream);
            output.writeUTF(methodParameter.getClassName());
            output.writeUTF(methodParameter.getMethodName());
            output.writeObject(methodParameter.getParameterTypes());
            output.writeObject(methodParameter.getArguments());

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream input = new ObjectInputStream(inputStream);
            return input.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
