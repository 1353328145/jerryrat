package fun.jexing.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ReflectionTool {
    /**
     * 获取所有标注了annotation的Class
     * @param annotation
     * @param packagePath
     * @return
     */
    public static Set<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotation, String packagePath){
        Set<Class<?>> set = new HashSet<>();
        StringBuffer pathBuffer = new StringBuffer(packagePath);
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packagePath.replace(".", "/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                if (url!=null && "file".equals(url.getProtocol())){
                    scanClass(set,annotation,url.getPath(),pathBuffer);
                }else{
                    throw new IOException("发现不支持的类型");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return set;
    }
    private static void scanClass(Set<Class<?>> set,Class<? extends Annotation> annotation,String packagePath, StringBuffer refPath) throws ClassNotFoundException {
        File[] files = new File(packagePath).listFiles( (file, name) -> (file.isFile() && name.endsWith(".class")) || file.isDirectory());
        for (File file : files) {
            if (file.isDirectory()){
                int size = refPath.length();
                refPath.append('.').append(file.getName());
                scanClass(set,annotation,file.getPath(),refPath);
                refPath.delete(size,refPath.length());
            }else{
                String name = file.getName();
                int size = refPath.length();
                refPath.append('.').append(name, 0, name.indexOf(".class"));
                Class<?> aClass = Class.forName(refPath.toString());
                if (aClass.getAnnotation(annotation)!=null){
                    set.add(aClass);
                }
                refPath.delete(size,refPath.length());
            }
        }
    }
    /**
     * 获取类加载器
     */
    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
