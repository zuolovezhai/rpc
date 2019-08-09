package rpc.v2.balance;

import java.util.List;
import java.util.Random;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class DefaultLoadBalance implements LoadBalance {
    @Override
    public String balance(List<String> addressList) {
        Random random = new Random();
        return addressList.get(random.nextInt(addressList.size()));
    }
}
