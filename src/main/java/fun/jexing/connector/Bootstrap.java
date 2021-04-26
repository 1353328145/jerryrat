package fun.jexing.connector;

import fun.jexing.config.ServerConfig;
import fun.jexing.container.ComponentContext;
import fun.jexing.container.Context;
import fun.jexing.utils.DateTool;

import java.util.Date;
import java.util.UUID;


public class Bootstrap {
    public static void main(String[] args)  {
        ServerConfig config = new ServerConfig();
        config.setScanPath("fun.jexing");
        HttpConnector httpConnector = new HttpConnector(config);
        Context context = new ComponentContext(config);
        httpConnector.setContext(context);
        httpConnector.start();
    }
}
