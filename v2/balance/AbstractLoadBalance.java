package rpc.v2.balance;

import java.util.List;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String balance(List<String> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            return null;
        }
        if (addressList.size() == 1) {
            return addressList.get(0);
        }
        return doLoad(addressList);
    }

    abstract String doLoad(List<String> addressList);
}
