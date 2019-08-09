package rpc.v1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class RpcUsedService {
    private Map<String, Object> proxyObjectMap = new HashMap();
    private Map<String, Class> classMap = new HashMap();
    private IOClient ioClient;

    public void setIoClient(IOClient ioClient) {
        this.ioClient = ioClient;
    }

    public void register(Class clazz) {
        String className = clazz.getName();
        classMap.put(className, clazz);
        if (!clazz.isInterface()) {
            throw new RuntimeException("暂时只支持接口类型");
        }

        try {
            RpcInvocationHandler handler = new RpcInvocationHandler();
            handler.setClazz(clazz);
            Object proxyInstance = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, handler);
            proxyObjectMap.put(className,  proxyInstance);
        } catch (Exception e) {

        }
    }

    public <T> T get(Class<T> clazz) {
        String className = clazz.getName();
        return (T) proxyObjectMap.get(className);
    }

    class RpcInvocationHandler implements InvocationHandler {
        private Class clazz;
        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MethodParameter methodParameter = new MethodParameter();
            methodParameter.setClassName(clazz.getName());
            methodParameter.setMethodName(method.getName());
            methodParameter.setArguments(args);
            methodParameter.setParameterTypes(method.getParameterTypes());
            return ioClient.invoke(methodParameter);
        }
    }
}
