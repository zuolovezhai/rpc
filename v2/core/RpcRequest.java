package rpc.v2.core;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = -459001602905341503L;
    private String className;
    private String methodName;
    private Object[] arguments;
    private Class<?>[] parameterTypes;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
