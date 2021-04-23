package fun.jexing.connector;

import fun.jexing.config.ServerConfig;

import java.io.IOException;
import java.net.URLDecoder;


public class Bootstrap {
    public static void main(String[] args) throws IOException {
        HttpConnector httpConnector = new HttpConnector(new ServerConfig());
        httpConnector.start();
    }
}
