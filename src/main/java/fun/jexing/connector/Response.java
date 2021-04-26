package fun.jexing.connector;

import fun.jexing.config.ServerConfig;
import fun.jexing.utils.DateTool;
import fun.jexing.utils.Tool;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Response implements HttpResponse{
    private Request request;
    private OutputStream output;
    private PrintWriter writer;
    private Map<String,String> headerMap;
    private int status;
    private String msg;
    private String contentType;
    private String charset;
    private int contentLength;
    private String space = "";
    private String end = "\r\n";
    private String colon= ":";
    private String dot = ".";
    private String msg404 = "<h1>404 not found</h1>";
    private ServerConfig config;
    public void finishResponse(){
        boolean f = msg != null;
        if (f){
            setContentLength(msg.length());
        }
        StringBuffer res = getFinishResult();
        if (f){
            res.append(msg);
        }
        try {
            output.write(res.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public StringBuffer getFinishResult(){
        //日期
        setHeader("Date", DateTool.currentDate());
        //服务器名称
        setHeader("Server", "jerryrat/0.1");
        //关闭连接请求头
        setHeader(HeaderUtil.CONNECTION_NAME, HeaderUtil.CONNECTION_CLOSE_VALUE);
        StringBuffer res = new StringBuffer();
        if (contentType != null){
            if (charset == null){
                headerMap.put(HeaderUtil.CONTENT_TYPE_NAME,contentType);
            }else{
                headerMap.put(HeaderUtil.CONTENT_TYPE_NAME,contentType + "; charset=" +charset);
            }
        }
        if (contentLength != -1){
            headerMap.put(HeaderUtil.CONTENT_LENGTH_NAME,contentLength + "");
        }
        //http版本
        String protocol = request.getParser().getProtocol();
        res.append(protocol).append(space).append(status).append(space).append(end);
        for (String key : headerMap.keySet()) {
            res.append(key).append(colon).append(space).append(headerMap.get(key)).append(end);
        }
        res.append(end);
        return res;
    }
    public void staticResourceResponse(String url){
        File file = new File(config.getWebRoot() + url);
        if (!file.exists() || !file.isFile()){
            //找不到，返回404页面
            setStatus(404);
            setMsg(msg404);
            finishResponse();
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[config.getBufferSize()];
            int len;
            setStatus(200);
            output.write(getFinishResult().toString().getBytes());
            while ((len = fileInputStream.read(buffer))!= -1){
                output.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Response(OutputStream output){
        this.output = output;
        contentLength = -1;
        headerMap = new HashMap<>();
    }

    public void setHeader(String name,String value){
        headerMap.put(name,value);
    }
    public void setRequest(Request request) {
        this.request = request;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCharacterEncoding() {
        return charset;
    }

    public String getContentType() {
        return contentType;
    }

    public PrintWriter getWriter() {
        if (writer == null){
            writer = new PrintWriter(output);
        }
        return writer;
    }

    public void setCharacterEncoding(String var1) {
        charset = var1;
    }

    public void setContentLength(int var1) {
        this.contentLength = var1;
    }

    public void setContentType(String var1) {
        this.contentType = var1;
    }

    public void setConfig(ServerConfig config) {
        this.config= config;
    }
}
