package fun.jexing.connector;

import fun.jexing.config.ServerConfig;
import fun.jexing.container.Context;
import fun.jexing.utils.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpConnector {
    private boolean isStopped;
    private ThreadPoolExecutor pool;
    private Context context;
    //配置类
    private ServerConfig config;
    HttpConnector(ServerConfig config){
        this.config = config;
        this.isStopped = false;
        initThreadPool();
        Logger.log("连接器对象已经创建...",HttpConnector.class);
    }
    private void initThreadPool(){
        int coreThreadNum = config.getCoreThreadNum();
        int maxThreadNum = config.getMaxThreadNum();
        long keepAliveTime = config.getKeepAliveTime();
        pool = new ThreadPoolExecutor(coreThreadNum,
                maxThreadNum,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(config.getBlockingQueueSize()),
                new ThreadPoolExecutor.AbortPolicy());//超出抛异常
        Logger.log("初始化线程池-->核心线程最大值:"+coreThreadNum + " 线程最大值:"+maxThreadNum + " 非核心线程存活时间:" + keepAliveTime,HttpConnector.class);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public void setConfig(ServerConfig config) {
        this.config = config;
    }

    //启动方法
    public void start(){
        int port= config.getPort();
        ServerSocket serverSocket = null;
        Logger.log("开始监听http请求...",HttpConnector.class);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            //异常退出
            System.exit(1);
        }
        while (!isStopped){
            //等待请求
            Socket clientSocket;
            try {
                //接收客户端请求
                clientSocket = serverSocket.accept();
                //开始一个线程，处理这个请求
                HttpProcessor processor = new HttpProcessor(this,clientSocket);
                processor.setContext(context);
                pool.execute(processor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
