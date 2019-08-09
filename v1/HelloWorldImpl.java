package rpc.v1;

import java.util.Random;

/**
 * @Description TODO
 * @Author zuoshengli
 **/
public class HelloWorldImpl implements HelloWorld {

    @Override
    public String hi() {
        return "ok";
    }

    @Override
    public int add(int a, int b) {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(new Random().nextInt(10000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int c = a + b;
        System.out.println(Thread.currentThread().getName() + " 耗时: " + (System.currentTimeMillis()-start));
        return c;
    }
}
