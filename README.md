# jerryrat 一个简单web容器

- 基本介绍

    本项目是一个嵌入式的web容器，基于java的bio阻塞式通信,封装了http请求,可提供HttpRequest 和 HttpResponse对象供实现了Component接口的组件使用
实现了通过url对静态资源和处理请求的组件( = servlet) 的访问，因为这是简单的实现，所以可能很多复杂的功能后没有做或者做的不完整

- 目录介绍

   - src下就是源代码
   
   - server-test模块 是一个测试使用的demo,里边演示了容器的基本使用

- 基本功能

    - 可以实现url对静态资源的映射
    
    - 可以实现根据url对component组件的访问
    
    - 可以实现注解反射调用组件,简化配置
    
    - 拦截器还未实现
    
    - 简单实现了基于cookie的HttpSession,详见server-test模块下由具体使用
    
- 具体说明

    -  虽然http/1.1可以keep-alive,但我没有实现，每次在所以响应头利默认有connection: close
    
    -  只支持get post options 类型请求
    
    -  总体来说项目基于线程池,线程池和其他参数都在ServerConfig中配置,需要使用时传入

    -  注解扫描功能,依赖了reflections包,之后可能会改成自己的实现(已修改)
    
- 其他
    
    推荐《深入剖析tomcat》这本书，讲的很不错
