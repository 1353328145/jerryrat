package jerryrat_test.start;

import fun.jexing.config.ServerConfig;
import fun.jexing.connector.Bootstrap;

public class Application {
    public static void main(String[] args) {
        ServerConfig config = new ServerConfig();
        //设置注解扫描路径
        config.setScanPath("jerryrat_test.start");
        Bootstrap.run(config);
    }
}
