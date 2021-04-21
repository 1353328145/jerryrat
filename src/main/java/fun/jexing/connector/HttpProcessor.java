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
    private void parseConnection(){

    }
    private void parseRequest(){

    }
    private void parseHeaders(){

    }
    public void process(){
        Request request = new Request();
        Response response = new Response(output);
        response.setRequest(request);
        //如果保持连接
        if (isKeepAlive){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            BufferedInputStream stream = new BufferedInputStream(socket.getInputStream());
            byte[] arr = new byte[2048];
            int len;
            while ((len = stream.read(arr)) != -1){
                System.out.println(new String(arr,0,len));
            }
            System.out.println("数据被接收");
//            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
