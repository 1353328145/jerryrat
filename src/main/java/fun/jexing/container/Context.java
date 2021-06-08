package fun.jexing.container;

import fun.jexing.config.ServerConfig;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;

public interface Context {
    //调用
    void invoke(HttpRequest request, HttpResponse response);

    //调用指定url的服务
    void invoke(String url,HttpRequest request, HttpResponse response);

    //加载组件
    void init(ServerConfig config);

    //判断url是否存在
    boolean exist(String url);

    //获取session
    HttpSession getSession(String sessionId);

    //获取配置
    ServerConfig getConfig();
}
