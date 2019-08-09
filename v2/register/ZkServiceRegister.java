package rpc.v2.register;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import rpc.v2.balance.DefaultLoadBalance;
import rpc.v2.balance.LoadBalance;
import rpc.v2.config.BasicConfig;
import rpc.v2.core.RpcRequest;
import rpc.v2.domain.ServiceType;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class ZkServiceRegister implements ServiceRegister {
    private CuratorFramework client;
    private static final String ROOT_PATH = "zuoshengli/simple-rpc";
    private LoadBalance loadBalance = new DefaultLoadBalance();

    public ZkServiceRegister() {
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);

        this.client = CuratorFrameworkFactory
                .builder()
                .connectString("172.16.44.30:21181")
                .sessionTimeoutMs(50000)
                .retryPolicy(policy)
                .namespace(ROOT_PATH)
                .build();
        this.client.start();
        System.out.println("zk启动正常");
    }

    @Override
    public void register(BasicConfig config) {
        String interfacePath = "/" + config.getInterfaceName();
        try {
            if (this.client.checkExists().forPath(interfacePath) == null) {
                //创建服务永久节点
                this.client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(interfacePath);

            }

            config.getMethods().forEach(method -> {
               try {
                   String methodPath = null;
                   ServiceType serviceType = config.getType();
                   if (serviceType == ServiceType.PROVIDER) {
                       //服务提供者，需要暴漏自己的ip、port信息，而消费端则不需要
                       String address = getServiceAddress(config);
                       methodPath = String.format("%s/%s/%s/%s", interfacePath, serviceType.getType(), method.getMethodName(), address);
                   } else {
                       methodPath = String.format("%s/%s/%s", interfacePath, serviceType.getType(), method.getMethodName());
                   }
                   System.out.println("zk path: [" + ROOT_PATH + methodPath + "]");
                   this.client.create()
                           .creatingParentsIfNeeded()
                           .withMode(CreateMode.EPHEMERAL)
                           .forPath(methodPath, "0".getBytes());

               } catch (Exception e) {
                    System.out.println(e.getMessage());
               }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    @Override
    public InetSocketAddress discovery(RpcRequest request, ServiceType nodeType) {
        String path = String.format("/%s/%s/%s", request.getClassName(), nodeType.getType(), request.getMethodName());
        try {
            List<String> addressList = this.client.getChildren().forPath(path);
            //采用负载均衡的方式获取服务提供者信息，不过并没有添加watcher监听模式
            String address = loadBalance.balance(addressList);
            if (address == null) {
                return null;
            }
            return parseAddress(address);
        } catch (Exception e) {

        }
        return null;
    }

    private String getServiceAddress(BasicConfig config) {
        String hostInfo = new StringBuilder()
                .append(config.getHost())
                .append(":")
                .append(config.getPort())
                .toString();
        return hostInfo;
    }

    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.valueOf(result[1]));
    }

    public void setLoadBalance(LoadBalance loadBalance) {
        //可以重设负载均衡策略
        this.loadBalance = loadBalance;
    }
}
