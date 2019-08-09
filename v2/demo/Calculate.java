package rpc.v2.demo;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public interface Calculate<T> {
    T add(T a, T b);
    T sub(T a, T b);
}
