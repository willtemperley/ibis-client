package org.jrc.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.servlet.ServletModule;

public abstract class AbstractGuiceServletModule extends ServletModule {

    private Properties props;

    /**
     * Gets a map of servlet parameters
     * 
     * @return
     */
    protected Map<String, String> getServletParams() {
        Properties properties = getRuntimeProperties();

        Map<String, String> params = new HashMap<String, String>();

        params.put("widgetset", properties.getProperty("widgetset"));
        params.put("productionMode", properties.getProperty("productionMode"));
        return params;
    }
    

    /**
     * Returns the properties from runtime.properties, loading them from file if
     * this hasn't already been done.
     * 
     * @return the runtime {@link Properties} 
     *  
     */
    protected Properties getRuntimeProperties() {

        if (props == null) {

            props = new Properties();

            InputStream stream = this.getClass().getClassLoader()
                    .getResourceAsStream("runtime.properties");
            try {
                props.load(stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return props;
    }

    @Provides
    @Named("context_path")
    String contextPath(ServletContext context) {
        return context.getContextPath();
    }


}