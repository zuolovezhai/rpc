package rpc.v2.core;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 1899795634187841048L;
    private T result;
    private Boolean isError;
    private String errorMessage;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Boolean getError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
