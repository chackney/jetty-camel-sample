package org.muckingabout;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import java.util.Map;

/**
 * Basic interface that allows us to abstract out the jetty impl
 *
 * No need to go any further, since we have no plans to swap jetty,
 * but in theory you would just have to re-implement the impl.
 *
 */
public interface HttpServerWrapper {
    public void startServer() throws Exception;
    public void stopServer() throws Exception;
    public  void addServlet(String servletName, String path, HttpServlet httpServlet, Map<String,String> initConfig);
    public  void addFilter(String filterName, Filter httpFilter, String [] paths);
}
