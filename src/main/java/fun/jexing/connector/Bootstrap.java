package fun.jexing.connector;

import fun.jexing.config.ServerConfig;
import fun.jexing.container.ComponentContext;
import fun.jexing.container.Context;


public class Bootstrap {
    public static void run(ServerConfig config)  {
        HttpConnector httpConnector = new HttpConnector(config);
        Context context = new ComponentContext(config);
        httpConnector.setContext(context);
        httpConnector.start();
    }
}
