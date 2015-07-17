package org.muckingabout;

import org.springframework.boot.SpringApplication;

/**
 *
 * @author chackey
 */
public class Application {

    public static void main (String ... args) {
        SpringApplication.run("classpath:/spring-context.xml", args);
    }

}
