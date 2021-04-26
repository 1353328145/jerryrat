package fun.jexing.container;

import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.utils.Logger;

public class Wrapper {
    private String url;
    private String path;
    private Component componentInstance;

    public Wrapper(String url, String path) throws Exception {
        this.url = url;
        this.path = path;
        Class<?> componentClass = Class.forName(path);
        componentInstance = (Component) componentClass.newInstance();
        componentInstance.init();
        Logger.log("初始化组件 " + this + " 成功!",Wrapper.class);
    }
    private Wrapper(){}
    public static Wrapper getInstance(String url, String path, Component component){
        Wrapper wrapper = new Wrapper();
        wrapper.path = path;
        wrapper.url = url;
        wrapper.componentInstance = component;
        return wrapper;
    }

    public void service(HttpRequest request, HttpResponse response){
        componentInstance.service(request,response);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Component getComponentInstance() {
        return componentInstance;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
