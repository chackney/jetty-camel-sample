package org.muckingabout;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author chackey
 */
public class Application {


    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main (String ... args) {
        SpringApplication.run("classpath:/spring-context.xml", args);

    }

}
