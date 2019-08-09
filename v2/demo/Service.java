package rpc.v2.demo;

import rpc.v2.core.RpcService;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class Service {
    public static void main(String[] args) {
        RpcService rpcService = new RpcService(10001);
        rpcService.addService(Calculate.class, new SimpleCalculate());
        rpcService.start();
    }
}
