package fun.jexing.connector;

import fun.jexing.config.ServerConfig;
import fun.jexing.utils.DateTool;
import fun.jexing.utils.HeaderUtil;

import java.io.*;
import java.util.HashMap;
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
    private String space = " ";
    private String end = "\r\n";
    private String colon= ":";
    private String dot = ".";
    private String msg404 = "<h1>404 not found</h1>";
    private ServerConfig config;
    @Override
    public void finishResponse(){
        boolean f = msg != null;
        if (f){
            setContentLength(msg.getBytes().length);
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

    @Override
    public void redirect(String url) {
        StringBuffer finishResult = setStatus(302).setHeader(HeaderUtil.LOCATION_NAME, url).getFinishResult();
        finishResponse();
    }

    @Override
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
        status = 200;
        headerMap = new HashMap<>();
    }

    @Override
    public HttpResponse setHeader(String name, String value){
        headerMap.put(name,value);
        return this;
    }
    public void setRequest(Request request) {
        this.request = request;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    @Override
    public HttpResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    @Override
    public HttpResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    @Override
    public String getCharacterEncoding() {
        return charset;
    }
    @Override
    public String getContentType() {
        return contentType;
    }
    @Override
    public PrintWriter getWriter() {
        if (writer == null){
            writer = new PrintWriter(output);
        }
        return writer;
    }
    @Override
    public HttpResponse setCharacterEncoding(String var1) {
        charset = var1;
        return this;
    }
    @Override
    public HttpResponse setContentLength(int var1) {
        this.contentLength = var1;
        return this;
    }
    @Override
    public HttpResponse setContentType(String var1) {
        this.contentType = var1;
        return this;
    }

    public void setConfig(ServerConfig config) {
        this.config= config;
    }
}
