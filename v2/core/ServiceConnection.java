package rpc.v2.core;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class ServiceConnection implements Runnable {
    private int port;
    /**
     * 服务关闭标记
     */
    private volatile boolean flag = true;
    /**
     * 服务端套接字
     */
    private ServerSocket serverSocket;
    /**
     * 网络处理器
     */
    private ServiceHandler serviceHandler;

    public void init(int port, ServiceHandler serviceHandler) {
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            throw new RuntimeException("启动失败: " + e.getMessage());
        }
        this.serviceHandler = serviceHandler;
        System.out.println("服务启动了");
    }
    @Override
    public void run() {
        while (flag) {
            try {
                Socket socket = serverSocket.accept();
                serviceHandler.handler(socket);
            } catch (Exception e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void destory() {
        System.out.println("服务端套接字关闭");
        this.flag = flag;
    }
}
