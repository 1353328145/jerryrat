package fun.jexing.connector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;;
import java.net.Socket;
import java.util.Map;

public class HttpProcessor implements Runnable{
    private HttpConnector connector;
    private Socket socket;
    private boolean isKeepAlive;
    private RequestStringParser parser;
    private BufferedInputStream input;
    private OutputStream output;
    HttpProcessor(HttpConnector connector,Socket socket){
        this.connector = connector;
        this.socket = socket;
        try {
            input = new BufferedInputStream(socket.getInputStream());
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private RequestStringParser getParser(){
        StringBuilder sb = new StringBuilder();
        int bufferSize = connector.getConfig().getBufferSize();
        byte[] buffer = new byte[bufferSize];
        int len;
        try {
            while ((len = input.read(buffer))!= -1){
                sb.append(new String(buffer,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RequestStringParser(sb);
    }
    private void parseConnection(){

    }
    public void process(){
        //创建请求对象
        Request request = new Request();

        //创建响应对象
        Response response = new Response(output);
        response.setRequest(request);
        //解析请求 , 从inputStream读出数据并解析
        parser = getParser();
        request.setParser(parser);
        Map<String, String> headerMap = parser.getHeaderMap();
        //处理cookie
        String cookie = headerMap.get("Cookie");
        if (cookie != null){
            Cookie[] cookies = Cookie.parseCookieHeader(cookie);
            request.setCookies(cookies);
        }
        if (false){
            //处理servlet
        }else{
            //处理静态资源
        }
        //如果保持连接
        if (!isKeepAlive){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        process();
    }
}
