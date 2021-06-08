package fun.jexing.container;

import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;

public interface Component {
    //初始化
    default void init(){}

    //服务器方法
    void service(HttpRequest request, HttpResponse response);
}
