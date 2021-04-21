package fun.jexing.connector;

import fun.jexing.config.ServerConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HttpConnector {
    private boolean isStopped;
    private ThreadPoolExecutor pool;
    //配置类
    private ServerConfig config;
    HttpConnector(ServerConfig config){
        this.config = config;
        this.isStopped = false;
        initThreadPool();
    }
    private void initThreadPool(){
        pool = new ThreadPoolExecutor(config.getCoreThreadNum(),
                config.getMaxThreadNum(),
                config.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(config.getBlockingQueueSize()),
                new ThreadPoolExecutor.AbortPolicy());//超出抛异常
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
                pool.execute(processor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
