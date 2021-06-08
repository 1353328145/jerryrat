# jerryrat 一个简单web容器

- 基本介绍

    本项目是一个嵌入式的web容器，基于java的bio阻塞式通信,封装了http请求,可提供HttpRequest 和 HttpResponse对象供实现了Component接口的组件使用。
实现通过url对静态资源和处理请求的组件( = servlet) 的访问，因为这是简单的实现，所以可能很多复杂的功能没有做或者做的不完整

- 目录介绍

   - src下就是源代码
   
   - server-test模块 是一个测试使用的demo,里边演示了容器的基本使用

- 基本功能
    - 可以实现重定向和转发

    - 可以实现url对静态资源的映射
    
    - 可以实现根据url对component组件的访问
    
    - 可以实现注解反射调用组件,简化配置。实例如下，可以实现接受一个参数然后用json格式返回
    
        ```java
          @HttpComponent(url = "/loadUser")
          public class AjaxHandle implements Component {
          
              @Override
              public void service(HttpRequest request, HttpResponse response) {
                  String username = request.getParameter("username");
                  response.setContentType("application/json");
                  response.setCharacterEncoding("UTF-8");
                  response.setMsg("{\"username\":\" "+username+"\" }");
                  response.finishResponse();
              }
          }
        ```
    
    - 拦截器还未实现
    
    - 简单实现了基于cookie的HttpSession,详见server-test模块下由具体使用
    
    - 增加了新功能，通过方法来处理请求，类似springMVC。当加上注解后会自动扫描,但是参数是固定的，如果参数无法匹配，则会装载失败。实例如下,可以实现一个返回json数据的接口
    
        ```java
            @HttpHandleClass
            public class MappingTest {
            
                @HttpMapping(url = "/get")
                public void test(HttpRequest request,HttpResponse response){
                    response.setMsg("{\"name\":\"张三\",\"password\":\"123456\"}");
                    response.setContentType("application/json; charset=UTF-8");
                    response.finishResponse();
                }
            }
        ```
    
- 具体说明

    -  虽然http/1.1可以keep-alive,但我没有实现，每次在所以响应头利默认有connection: close
    
    -  只支持get post options 类型请求
    
    -  总体来说项目基于线程池,线程池和其他参数都在ServerConfig中配置,需要使用时传入

    -  注解扫描功能,依赖了reflections包,之后可能会改成自己的实现(已修改)
    
    -  默认的静态资源根目录是maven项目resources目录下的web目录，可以通过SeverConfig修改
    
- 其他
    
    推荐《深入剖析tomcat》这本书，讲的很不错
