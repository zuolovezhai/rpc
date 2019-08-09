package rpc.v1;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class Service {
    public static void main(String[] args) {
        RpcExploreService rpcExploreService = new RpcExploreService();
        rpcExploreService.explore("rpc.v1.HelloWorld", new HelloWorldImpl());
        try {
            Runnable ioService = new IOService(rpcExploreService, 10001);
            new Thread(ioService).start();
        } catch (Exception e) {

        }
    }
}
