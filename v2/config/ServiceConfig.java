package rpc.v2.config;

import com.alibaba.fastjson.JSON;
import rpc.v2.core.RpcService;
import rpc.v2.domain.ServiceType;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class ServiceConfig<T> extends BasicConfig implements Serializable {
    private static final long serialVersionUID = -4130301873348733367L;
    private T ref;
    /**
     * 统计调用次数
     */
    private int count;

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static <T> ServiceConfig convert(String interfaceName, Class<T> interfaceClass, T ref, RpcService rpcService) {
        ServiceConfig<T> serviceConfig= new ServiceConfig();
        serviceConfig.setRef(ref);
        serviceConfig.setInterfaceName(interfaceName);
        serviceConfig.setInterfaceClass(interfaceClass);
        serviceConfig.setCount(0);
        serviceConfig.setMethods(MethodConfig.convert(interfaceClass.getMethods()));
        serviceConfig.setPort(rpcService.getPort());
        serviceConfig.setType(ServiceType.PROVIDER);

        try {
            InetAddress addr = InetAddress.getLocalHost();
            serviceConfig.setHost(addr.getHostAddress().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceConfig;
    }
}
