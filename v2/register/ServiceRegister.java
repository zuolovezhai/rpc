package rpc.v2.register;

import rpc.v2.config.BasicConfig;
import rpc.v2.core.RpcRequest;
import rpc.v2.domain.ServiceType;

import java.net.InetSocketAddress;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public interface ServiceRegister {
    /**
     * 服务注册
     * @param config
     */
    void register(BasicConfig config);

    InetSocketAddress discovery(RpcRequest request, ServiceType nodeType);
}
