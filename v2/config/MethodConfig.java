package rpc.v2.config;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 方法的配置
 * @Author zuoshengli
 **/
public class MethodConfig implements Serializable {

    private static final long serialVersionUID = -2568008465175821521L;
    private String methodName;
    private List<ArgumentConfig> argumentConfigs;

    private boolean isReturn;

    private Class<?> returnType;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ArgumentConfig> getArgumentConfigs() {
        return argumentConfigs;
    }

    public void setArgumentConfigs(List<ArgumentConfig> argumentConfigs) {
        this.argumentConfigs = argumentConfigs;
    }

    public boolean getReturn() {
        return isReturn;
    }

    public void setReturn(boolean aReturn) {
        isReturn = aReturn;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public static List<MethodConfig> convert(Method[] methods) {
        List<MethodConfig> methodConfigList = new ArrayList(methods.length);
        for (Method method : methods) {
            MethodConfig methodConfig = new MethodConfig();
            methodConfig.setMethodName(method.getName());
            Class<?> returnType = method.getReturnType();
            String returnName = returnType.getName();
            if ("void".equals(returnName)) {
                methodConfig.setReturn(false);
            } else {
                methodConfig.setReturn(true);
            }
            methodConfig.setReturnType(returnType);
            methodConfig.setArgumentConfigs(convert(method.getParameters()));
            methodConfigList.add(methodConfig);
        }
        return methodConfigList;
    }

    private static List<ArgumentConfig> convert(Parameter[] parameters) {
        List<ArgumentConfig> argumentConfigList = new ArrayList();
        for (int i = 0; i < parameters.length; i++) {
            ArgumentConfig argumentConfig = new ArgumentConfig();
            argumentConfig.setIndex(i);
            argumentConfig.setType(parameters[i].getType().getName());
            argumentConfigList.add(argumentConfig);
        }
        return argumentConfigList;
    }
}
