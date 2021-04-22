package fun.jexing.config;

public class ServerConfig {
    //端口
    private int port;
    //线程池设置
    private int maxThreadNum;
    private int coreThreadNum;
    private long keepAliveTime;
    private int blockingQueueSize;
    //缓冲区大小
    private int bufferSize;
    private static final int DEFAULT_port = 80;

    public ServerConfig(){
        this(DEFAULT_port);
    }
    public ServerConfig(int port){
        this.port = port;
        //默认设置
        //最大线程数
        this.maxThreadNum = 800;
        //核心线程数
        this.coreThreadNum = 200;
        //非核心线程存活 秒
        this.keepAliveTime = 60;
        //阻塞队列容量
        this.blockingQueueSize = 50;
        //缓冲区大小
        this.bufferSize = 1024;
    }
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxThreadNum() {
        return maxThreadNum;
    }

    public void setMaxThreadNum(int maxThreadNum) {
        this.maxThreadNum = maxThreadNum;
    }

    public int getCoreThreadNum() {
        return coreThreadNum;
    }

    public void setCoreThreadNum(int coreThreadNum) {
        this.coreThreadNum = coreThreadNum;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getBlockingQueueSize() {
        return blockingQueueSize;
    }

    public void setBlockingQueueSize(int blockingQueueSize) {
        this.blockingQueueSize = blockingQueueSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
