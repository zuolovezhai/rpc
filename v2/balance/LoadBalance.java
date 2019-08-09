package rpc.v2.balance;

import java.util.List;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public interface LoadBalance {
    String balance(List<String> addressList);
}
