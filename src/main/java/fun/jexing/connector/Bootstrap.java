package fun.jexing.connector;

import fun.jexing.config.ServerConfig;


public class Bootstrap {
    public static void main(String[] args) {
        HttpConnector httpConnector = new HttpConnector(new ServerConfig());
        httpConnector.start();
//        StringBuilder sb = new StringBuilder("GET /index.html?abc=&ccc=100 HTTP/1.1\r\n" +
//                "Host: localhost\r\n" +
//                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:87.0) Gecko/20100101 Firefox/87.0\r\n" +
//                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n" +
//                "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\r\n" +
//                "Accept-Encoding: gzip, deflate\r\n" +
//                "Connection: keep-alive\r\n" +
//                "Upgrade-Insecure-Requests: 1\r\n" +
//                "\r\n");
//        RequestStringParser parser = new RequestStringParser(sb);
    }
}
