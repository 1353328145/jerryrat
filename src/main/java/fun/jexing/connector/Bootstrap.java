package fun.jexing.connector;

import fun.jexing.config.ServerConfig;


public class Bootstrap {
    public static void main(String[] args) {
        HttpConnector httpConnector = new HttpConnector(new ServerConfig());
        httpConnector.start();
    }
}
