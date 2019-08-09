package rpc.v2.serialize;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class HessianSerialize implements SerializeProtocol {
    @Override
    public <T> byte[] serialize(Class<T> clazz, T t) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        try {
            hessian2Output.writeObject(t);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                hessian2Output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            byteArrayOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(inputStream);
        try {
            T t = (T) hessian2Input.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                hessian2Input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
