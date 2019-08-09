package rpc.v2.config;

import rpc.v2.core.ProxyInstance;
import rpc.v2.domain.ServiceType;

import java.io.Serializable;
import java.lang.reflect.Proxy;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class ClientConfig<T> extends BasicConfig implements Serializable {
    private static final long serialVersionUID = 7004390800101504219L;
    private T proxy;
    public void setProxy(T proxy) {
        this.proxy = proxy;
    }

    public T getProxy() {
        return this.proxy;
    }

    public static <T> ClientConfig<T> convert(Class<T> interfaceClass, ProxyInstance invocationHandler) {
        ClientConfig<T> config = new ClientConfig();
        config.setVersion("default");
        config.setInterfaceClass(interfaceClass);
        config.setInterfaceName(interfaceClass.getName());
        config.setMethods(MethodConfig.convert(interfaceClass.getMethods()));
        config.setType(ServiceType.CONSUMER);

        Object proxy = Proxy.newProxyInstance(ClientConfig.class.getClassLoader(),
                new Class<?>[]{interfaceClass},
                invocationHandler);
        config.setProxy((T) proxy);
        return config;
    }
}
