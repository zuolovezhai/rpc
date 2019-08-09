package rpc.v1;

import java.util.Random;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class Client {
    public static void main(String[] args) {
        RpcUsedService rpcUsedService = new RpcUsedService();
        rpcUsedService.register(HelloWorld.class);
        try {
            IOClient ioClient = new IOClient("172.16.44.30", 10001);
            rpcUsedService.setIoClient(ioClient);
            HelloWorld helloworld = rpcUsedService.get(HelloWorld.class);
            for (int i = 0; i < 100; i++) {
                new Thread(() -> {
                    long start = System.currentTimeMillis();
                    int a = new Random().nextInt(100);
                    int b = new Random().nextInt(100);
                    int c = helloworld.add(a, b);
                    System.out.println("a: " + a + ", b: " + b + ", c: " + c + ", 耗时: " + (System.currentTimeMillis()-start));
                }).start();
            }
        } catch (Exception e) {

        }
    }
}

