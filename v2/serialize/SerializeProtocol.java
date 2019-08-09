package rpc.v2.serialize;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public interface SerializeProtocol {
    /**
     * 序列化
     * @param clazz
     * @param t
     * @param <T>
     * @return
     */
    <T> byte[] serialize(Class<T> clazz, T t);

    /**
     * 反序列化
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
