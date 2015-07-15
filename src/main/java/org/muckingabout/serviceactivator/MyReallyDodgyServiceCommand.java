package org.muckingabout.serviceactivator;

import java.util.Random;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * Created by CLHACKNE on 15/07/2015.
 */
public class MyReallyDodgyServiceCommand extends HystrixCommand<String> {

    private static Random random = new Random(System.currentTimeMillis());

    public MyReallyDodgyServiceCommand() {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("dodgyCommandGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("reallyDodgyCommand")));
    }

    @Override
    protected String run() throws Exception {
        if(random.nextBoolean()) {
            throw new Exception("Opps that went wrong");
        }
        return "Wahoo";
    }
}
