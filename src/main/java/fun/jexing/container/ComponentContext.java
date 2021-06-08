package fun.jexing.container;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.annotation.HttpHandleClass;
import fun.jexing.annotation.HttpMapping;
import fun.jexing.config.ServerConfig;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.utils.Logger;
import fun.jexing.utils.ReflectionTool;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ComponentContext implements Context{
    private Map<String,Wrapper> components;
    private ConcurrentHashMap<String,HttpSession> sessionMap;
    private ServerConfig config;
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
        invoke(request.getRequestURI(),request,response);
    }

    @Override
    public void invoke(String url, HttpRequest request, HttpResponse response) {
            Wrapper wrapper = components.get(url);
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
        this.config = config;
        String scanPath = config.getScanPath();
        if (scanPath == null){
            Logger.log("未指定注解扫描路径...", ComponentContext.class);
        }else{
            try {
                initAnnotation(scanPath);
                initHttpHandleClass(scanPath);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.log("初始化组件失败... : ", ComponentContext.class);
            }
        }
    }
    //注册组件
    private void initAnnotation(String path) throws IllegalAccessException, InstantiationException {
        Set<Class<?>> set = ReflectionTool.getClassByAnnotation(HttpComponent.class, path);
        for (Class<?> aClass : set) {
            HttpComponent annotation = aClass.getAnnotation(HttpComponent.class);
            String url = annotation.url();
            if ("".equals(url)){ continue; }
            Component component= (Component) aClass.newInstance();
            Wrapper instance = Wrapper.getInstance(url, aClass.getName(), component);
            Logger.log("初始化组件 " + instance + " 成功!",Wrapper.class);
            components.put(url,instance);
        }
    }
    //注册Handle映射类
    private void initHttpHandleClass(String path) throws IllegalAccessException, InstantiationException {
        Set<Class<?>> classSet = ReflectionTool.getClassByAnnotation(HttpHandleClass.class,path);
        for (Class<?> aClass : classSet) {
            Object instance = null;
            for (Method declaredMethod : aClass.getDeclaredMethods()) {
                HttpMapping annotation = declaredMethod.getAnnotation(HttpMapping.class);
                if (annotation != null){
                    Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                    if (parameterTypes.length==2
                            && parameterTypes[0].getName().equals("fun.jexing.connector.HttpRequest")
                            && parameterTypes[1].getName().equals("fun.jexing.connector.HttpResponse")){
                        instance = instance == null ? aClass.newInstance() : instance;
                        String url = annotation.url();
                        Wrapper wrapper = Wrapper.getInstance(url, aClass.getName(), instance, declaredMethod);
                        Logger.log("初始化组件 " + wrapper + " 成功!",Wrapper.class);
                        components.put(url,wrapper);
                    }else{
                        Logger.log(declaredMethod + " 因为参数问题无法装载容器",Wrapper.class);
                    }
                }
            }
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

    @Override
    public ServerConfig getConfig() {
        return this.config;
    }
}
