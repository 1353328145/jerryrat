package fun.jexing.connector;

import fun.jexing.container.Context;
import fun.jexing.utils.Logger;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

public class HttpProcessor implements Runnable{
    private HttpConnector connector;
    private Socket socket;
    private boolean isKeepAlive;
    private RequestStringParser parser;
    private BufferedInputStream input;
    private OutputStream output;
    private Context context;
    public static String SESSIONID = "jsession";
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

    public void setContext(Context context) {
        this.context = context;
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
            Logger.log("异常请求关闭客户端连接...",HttpProcessor.class);
            throw new IOException();
        }
        sb.append(new String(buffer,0,len, ec));
        return new RequestStringParser(sb,ec);
    }
    private void ackOptions(){
        try {
            output.write("HTTP/1.1 200 OK\r\nAllow: GET,POST,OPTIONS\r\n\r\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void process(){
        //创建响应对象
        Response response = new Response(output);
        try {
            //创建请求对象
            Request request = new Request();
            //解析请求 , 从inputStream读出数据并解析
            parser = getParser();
            Logger.log("收到客户端请求: " + parser.getMethod() + " - " + parser.getUrl(),HttpProcessor.class);
            if ("OPTIONS".equals(parser.getMethod())){
                ackOptions();
                return;
            }
            request.setParser(parser);
            request.setServerConfig(connector.getConfig());
            request.setSocket(socket);
            request.setContext(context);
            response.setRequest(request);
            response.setConfig(connector.getConfig());
            Map<String, String> headerMap = parser.getHeaderMap();
            //处理cookie
            String cookie = headerMap.get("Cookie");
            if (cookie != null){
                Cookie[] cookies = Cookie.parseCookieHeader(cookie);
                request.setCookies(cookies);
                if (request.getSessionIdCookie() == null){
                    //设置cookie
                    createSessionCookie(response);
                }
            }else{
                //设置cookie
                createSessionCookie(response);
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
            String url = parser.getUrl();
            if (context.exist(url)){
                try {
                    context.invoke(request,response);
                }catch (Exception e){
                    e.printStackTrace();
                    response.setStatus(500);
                    response.setMsg(e.toString());
                    response.finishResponse();
                }
            }else{
                response.staticResourceResponse(url);
            }
        }catch (Exception ignored){
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
    private void createSessionCookie(Response response){
        String value = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(SESSIONID, value);
        cookie.setHttpOnly(true);
        response.setHeader("Set-Cookie",cookie.toString());
        //创建在context中创建session对象
        context.getSession(value);
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
