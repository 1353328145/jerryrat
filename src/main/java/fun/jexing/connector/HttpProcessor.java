package fun.jexing.connector;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class HttpProcessor implements Runnable{
    private HttpConnector connector;
    private Socket socket;
    private boolean isKeepAlive;
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
    private void parseRequest(){
        //从inputStream读出数据并解析
        RequestStringParser parser = getParser();

    }
    private void parseHeaders(){

    }
    public void process(){
        Request request = new Request();
        Response response = new Response(output);
        response.setRequest(request);
        //解析请求
        parseRequest();
        //如果保持连接
//        if (!isKeepAlive){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
    }

    public void run() {
        process();
    }
}
