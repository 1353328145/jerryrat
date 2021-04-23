package fun.jexing.connector;

import fun.jexing.utils.DateTool;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpProcessor implements Runnable{
    private HttpConnector connector;
    private Socket socket;
    private boolean isKeepAlive;
    private RequestStringParser parser;
    private BufferedInputStream input;
    private OutputStream output;
    private boolean finish;
    HttpProcessor(HttpConnector connector,Socket socket){
        finish = true;
        this.connector = connector;
        this.socket = socket;
        try {
            input = new BufferedInputStream(socket.getInputStream());
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private RequestStringParser getParser() throws IOException {
        StringBuilder sb = new StringBuilder();
        int bufferSize = connector.getConfig().getBufferSize();
        String ec = connector.getConfig().getEc();
        byte[] buffer = new byte[bufferSize];
        int len = input.read(buffer);
        if (len <= 0){
            input.close();
            socket.close();
            throw new IOException();
        }
        sb.append(new String(buffer,0,len, ec));
        return new RequestStringParser(sb,ec);
    }
    public void process(){

        //创建响应对象
        Response response = new Response(output);
        try {
            //创建请求对象
            Request request = new Request();
            //解析请求 , 从inputStream读出数据并解析
            parser = getParser();
            request.setParser(parser);
            request.setServerConfig(connector.getConfig());
            request.setSocket(socket);
            response.setRequest(request);
            response.setConfig(connector.getConfig());
            Map<String, String> headerMap = parser.getHeaderMap();
            //处理cookie
            String cookie = headerMap.get("Cookie");
            if (cookie != null){
                Cookie[] cookies = Cookie.parseCookieHeader(cookie);
                request.setCookies(cookies);
            }
            //判断是否关闭连接
            boolean isHttp11 = "HTTP/1.1".equals(parser.getProtocol());
            if (isHttp11){
                //判断持久连接 只是判断而已  但不支持。。。
                if ("keep-alive".equals(headerMap.get(HeaderUtil.CONNECTION_NAME))){
                    isKeepAlive = true;
                }
                //检查Expect: 100-continue请求头
                if (HeaderUtil.EXPECT_100_VALUE.equals(headerMap.get(HeaderUtil.EXPECT_NAME))){
                    ack();
                }
            }
            response.setHeader("Date", DateTool.currentData());
            response.setHeader("Server", "jerryrat/0.1");
            response.setHeader(HeaderUtil.CONNECTION_NAME, HeaderUtil.CONNECTION_CLOSE_VALUE);
            response.staticResourceResponse(parser.getUrl());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //直接关闭
            try {
                input.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送确认消息
     */
    private void ack() throws IOException {
        output.write("HTTP/1.1 100 Continue\r\n".getBytes());
    }

    public void run() {
        process();
    }
}
