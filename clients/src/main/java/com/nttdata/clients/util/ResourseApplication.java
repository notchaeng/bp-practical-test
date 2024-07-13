package com.nttdata.clients.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourseApplication {

    public static final Properties properties = new Properties();
    static final ClassLoader loader = Thread.currentThread().getContextClassLoader();

    static {
        try {
            InputStream stream = loader.getResourceAsStream("messages.properties");
            properties.load(stream);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read in config file", e);
        }
    }
}
