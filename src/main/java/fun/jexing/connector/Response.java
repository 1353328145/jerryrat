package fun.jexing.connector;

import fun.jexing.config.ServerConfig;
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
    private String contentType;
    private String space = "";
    private String end = "\r\n";
    private String colon= ":";
    private String dot = ".";
    private String msg404 = "<h1>404 not found</h1>";
    private ServerConfig config;

    public void finishResponse(){
        finishResponse(200,null);
    }
    public void finishResponse(int code,String msg){
        StringBuffer res = getFinishResult(code,msg == null?0:msg.length());
        if (msg != null){
            res.append(msg);
        }
        try {
            output.write(res.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public StringBuffer getFinishResult(int code,int contentLength){
        StringBuffer res = new StringBuffer();
        if (contentType != null && contentLength != -1){
            setHeader(HeaderUtil.CONTENT_TYPE_NAME,contentType);
            setHeader(HeaderUtil.CONTENT_LENGTH_NAME,contentLength + "");
        }
        //http版本
        String protocol = request.getParser().getProtocol();
        res.append(protocol).append(space).append(code).append(space).append(end);
        for (String key : headerMap.keySet()) {
            res.append(key).append(colon).append(space).append(headerMap.get(key)).append(end);
        }
        res.append(end);
        return res;
    }
    public void staticResourceResponse(String url){
        File file = new File(config.getWebRoot() + url);
        if (!file.exists() || !file.isFile()){
            finishResponse(404,msg404);
            return;
        }
        String fileName = file.getName();
        contentType = Tool.getContentType(fileName.substring(fileName.indexOf(dot)));
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[config.getBufferSize()];
            int len;
            output.write(getFinishResult(200,-1).toString().getBytes());
            while ((len = fileInputStream.read(buffer))!= -1){
                output.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Response(OutputStream output){
        this.output = output;
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

    public String getCharacterEncoding() {
        return null;
    }

    public String getContentType() {
        return null;
    }

    public PrintWriter getWriter() {
        if (writer == null){
            writer = new PrintWriter(output,true);
        }
        return writer;
    }

    public void setCharacterEncoding(String var1) {

    }

    public void setContentLength(int var1) {

    }

    public void setContentType(String var1) {
        this.contentType = var1;
    }

    public void setBufferSize(int var1) {

    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {

    }

    public void resetBuffer() {

    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {

    }

    public void setLocale(Locale var1) {

    }

    public Locale getLocale() {
        return null;
    }

    public void setConfig(ServerConfig config) {
        this.config= config;
    }
}
