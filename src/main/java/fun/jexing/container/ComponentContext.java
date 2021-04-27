package fun.jexing.container;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.config.ServerConfig;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.utils.Logger;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentContext implements Context{
    private Map<String,Wrapper> components;
    private ConcurrentHashMap<String,HttpSession> sessionMap;
    public ComponentContext(ServerConfig config){
        components = new HashMap<>();
        sessionMap = new ConcurrentHashMap<>();
        Logger.log("初始化Component容器", ComponentContext.class);
        init(config);
    }
    private void addComponent(String url,String path) throws Exception {
        if (url == null){
            throw new RuntimeException("url不能为空");
        }
        Wrapper wrapper = new Wrapper(url,path);
        components.put(url,wrapper);
    }
    @Override
    public void invoke(HttpRequest request, HttpResponse response) {
        Wrapper wrapper = components.get(request.getRequestURI());
        wrapper.service(request,response);
    }

    @Override
    public void init(ServerConfig config) {
        Logger.log("开始初始化组件...", ComponentContext.class);
        //填充map
        Map<String, String> componentMap = config.getComponentMap();
        for (String s : componentMap.keySet()) {
            String path = componentMap.get(s);
            try {
                addComponent(s, path);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.log("初始化组件: " + s + " ---> " + path + " 失败!", ComponentContext.class);
            }
        }
        String scanPath = config.getScanPath();
        if (scanPath == null){
            Logger.log("未指定注解扫描路径...", ComponentContext.class);
        }else{
            try {
                initAnnotation(scanPath);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.log("注解未找到... : ", ComponentContext.class);
            }
        }

    }
    //注解组件
    private void initAnnotation(String path) throws IllegalAccessException, InstantiationException {
        Reflections f = new Reflections(path);
        Set<Class<?>> set = f.getTypesAnnotatedWith(HttpComponent.class);
        for (Class<?> aClass : set) {
            Class<?>[] interfaces = aClass.getInterfaces();
            if (Arrays.stream(interfaces).filter(s ->
                    "fun.jexing.container.Component".equals(s.getName())).count()!=1){
                //没有实现Component接口,跳过
                continue;
            }
            HttpComponent annotation = aClass.getAnnotation(HttpComponent.class);
            String url = annotation.url();
            if ("".equals(url)){ continue; }
            Component component = (Component) aClass.newInstance();
            Wrapper instance = Wrapper.getInstance(url, aClass.getName(), component);
            Logger.log("初始化组件 " + instance + " 成功!",Wrapper.class);
            components.put(url,instance);
        }
    }

    @Override
    public boolean exist(String url) {
        return components.containsKey(url);
    }

    @Override
    public HttpSession getSession(String sessionId) {
        if (sessionId == null){
            return null;
        }
        HttpSession httpSession =sessionMap.get(sessionId);
        if (httpSession == null){
            HttpSession value = new HttpSessionImpl();
            sessionMap.put(sessionId, value);
            return value;
        }
        return httpSession;
    }
}
