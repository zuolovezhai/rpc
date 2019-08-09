package rpc.v2.core;

import rpc.v2.config.ServiceConfig;
import rpc.v2.protocol.DefaultMessageProtocol;
import rpc.v2.protocol.MessageProtocol;
import rpc.v2.register.ServiceRegister;
import rpc.v2.register.ZkServiceRegister;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class RpcService {
    private Map<String, ServiceConfig> serviceConfigMap = new HashMap();
    private int port;
    private ServiceRegister serviceRegister;

    private ServiceConnection serviceConnection;
    private ServiceHandler serviceHandler;

    public RpcService(int port) {
        this.port = port;
        this.serviceHandler = new ServiceHandler(this);
        this.serviceHandler.setMessageProtocol(new DefaultMessageProtocol());

        this.serviceRegister = new ZkServiceRegister();
    }

    public void setMessageProtocol(MessageProtocol messageProtocol) {
        if (this.serviceHandler == null) {
            throw new RuntimeException("套接字处理器无效");
        }
        this.serviceHandler.setMessageProtocol(messageProtocol);
    }

    public <T> void addService(Class<T> interfaceClass, T ref) {
        String interfaceName = interfaceClass.getName();
        ServiceConfig serviceConfig = ServiceConfig.convert(interfaceName, interfaceClass, ref, this);
        serviceConfigMap.put(interfaceName, serviceConfig);
    }

    private void register() {
        serviceConfigMap.values().forEach(serviceRegister::register);
    }

    public void start() {
        this.register();
        System.out.println("服务器注册完成");
        this.serviceConnection = new ServiceConnection();
        this.serviceConnection.init(port, serviceHandler);
        new Thread(serviceConnection).start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            RpcService.this.destory();
        }));
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public <K, V> RpcResponse invoke(RpcRequest request) {
        if (request == null) {
            RpcResponse<V> response = new RpcResponse();
            response.setResult(null);
            response.setError(true);
            response.setErrorMessage("未知异常");
            return response;
        }

        String className = request.getClassName();
        ServiceConfig<K> serviceConfig = serviceConfigMap.get(className);
        K ref = serviceConfig.getRef();
        try {
            Method method = ref.getClass().getMethod(
                    request.getMethodName(),
                    request.getParameterTypes());
            V result = (V) method.invoke(ref, request.getArguments());
            RpcResponse<V> response = new RpcResponse();
            response.setResult(result);
            response.setError(false);
            response.setErrorMessage("");
            return response;
        } catch (Exception e) {

        }
        return null;
    }

    public void destory() {
        this.serviceConnection.destory();
        System.out.println("服务端关闭了");
    }

}
