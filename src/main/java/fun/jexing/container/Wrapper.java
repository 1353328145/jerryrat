package fun.jexing.container;

import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.connector.Response;
import fun.jexing.utils.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Wrapper {
    private String url;
    private String path;
    private Component componentInstance;
    private boolean enable;
    private Object httpHandleInstance;
    private Method mappingMethod;

    /**
     * 初始化Wrapper
     * @param url
     * @param path
     * @throws Exception
     */
    public Wrapper(String url, String path) throws Exception {
        this.url = url;
        this.path = path;
        this.enable = false;
        Class<?> componentClass = Class.forName(path);
        componentInstance = (Component) componentClass.newInstance();
        componentInstance.init();
        Logger.log("初始化组件 " + this + " 成功!",Wrapper.class);
    }
    private Wrapper(){}

    public static Wrapper getInstance(String url, String path, Component component){
        Wrapper wrapper = new Wrapper();
        wrapper.path = path;
        wrapper.enable = false;
        wrapper.url = url;
        wrapper.componentInstance = component;
        return wrapper;
    }

    public static Wrapper getInstance(String url,String path,Object instance,Method method){
        Wrapper wrapper = new Wrapper();
        wrapper.enable = true;
        wrapper.url = url;
        wrapper.path = path;
        wrapper.httpHandleInstance = instance;
        wrapper.mappingMethod = method;
        return wrapper;
    }

    public void service(HttpRequest request, HttpResponse response){
        if (enable){
            try {
                mappingMethod.invoke(httpHandleInstance,request,response);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                response.setMsg(Response.msg500);
                response.setStatus(500);
                response.finishResponse();
            }
        }else{
            componentInstance.service(request,response);
        }
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
        if (enable){
            return "{" +
                    "url='" + url + '\'' +
                    ", method='" + mappingMethod + '\'' +
                    '}';
        }else{
            return "{" +
                    "url='" + url + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }
}
