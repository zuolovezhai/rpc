package rpc.v2.core;

import rpc.v2.config.ClientConfig;
import rpc.v2.domain.ServiceType;
import rpc.v2.protocol.DefaultMessageProtocol;
import rpc.v2.register.ServiceRegister;
import rpc.v2.register.ZkServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class RpcClient {
    private Map<String, ClientConfig> clientConfigMap = new HashMap();
    private ServiceRegister serviceRegister;
    private ClientHandler clientHandler;
    public RpcClient() {
        this.serviceRegister = new ZkServiceRegister();
        this.clientHandler = new ClientHandler(this);
        this.clientHandler.setMessageProtocol(new DefaultMessageProtocol());
    }

    public <T> void subscribe(Class<T> clazz) {
        String interfaceName = clazz.getName();
        ProxyInstance invocationHandler = new ProxyInstance(this, clazz);
        ClientConfig<T> clientConfig = ClientConfig.convert(clazz, invocationHandler);
        clientConfigMap.put(interfaceName, clientConfig);
    }

    private void register() {
        clientConfigMap.values().forEach(serviceRegister::register);
    }

    public void start() {
        this.register();
    }

    public InetSocketAddress discovery(RpcRequest request) {
        return serviceRegister.discovery(request, ServiceType.PROVIDER);
    }

    public RpcResponse invoke(RpcRequest request, InetSocketAddress address) {
        return this.clientHandler.invoke(request, address);
    }

    public <T> T getInstance(Class<T> clazz) {
        return (T) clientConfigMap.get(clazz.getName()).getProxy();
    }
}
