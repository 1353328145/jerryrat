package fun.jexing.connector;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RequestStringParser {
    private StringBuilder sb;
    private static String end = "\r\n";
    private String method;
    private String url;
    private String protocol;
    private Map<String,String> parameterMap;
    private Map<String,String> headerMap;
    //当请求为post时
    private String body;
    private String ec;
    private boolean isPost;
    public RequestStringParser(StringBuilder sb,String ec){
        this.sb = sb;
        parameterMap = new HashMap<>();
        headerMap = new HashMap<>();
        this.ec = ec;
        parse();
    }
    public int requestLine(){
        int endIndex = sb.indexOf(end);
        byte switchNum = 0;
        int left = 0;
        for (int i = 0; i <= endIndex; i++) {
            char c = sb.charAt(i);
            if (c == ' ' || c == '\r'){
                switch (switchNum){
                    case 0:
                        method = sb.substring(left,i);
                        break;
                    case 1:
                        //单独处理url,分离参数与url
                        parseUrl(left,i);
                        break;
                    case 2:
                        protocol =  sb.substring(left,i);
                        break;
                }
                switchNum++;
                left = i + 1;
            }
        }
        return endIndex;
    }
    public void parseUrl(int left ,int right){
        //判断是否是post请求,post不解析url的参数
        isPost = "POST".equals(method);
        //出现？的坐标
        int p = sb.indexOf("?",left);
        //不带有参数
        if (p == -1 || p >= right){
            url = sb.substring(left,right);
        }else{
            url = sb.substring(left,p);
            //post请求直接返回
            if (isPost){ return; }
            parseUrlParameter(p + 1,right);
        }
    }
    private void parseUrlParameter(int left, int right){
        String name = null;
        String value;
        for (int i = left; i < right; i++) {
            char c = sb.charAt(i);
            if (c == '='){
                name = sb.substring(left,i);
                left = i + 1;
            }else if (c == '&'){
                value = sb.substring(left,i);
                parameterMap.put(name,value);
                left = i + 1;
            }
        }
        value = sb.substring(left,right);
        parameterMap.put(name,value);
        if ("GET".equals(method)){
            for (String s : parameterMap.keySet()) {
                try {
                    parameterMap.put(s, URLDecoder.decode(parameterMap.get(s),ec));
                } catch (UnsupportedEncodingException ignored) {}
            }
        }
    }
    public void parse(){
        int lastIndex = requestLine() + 2;
        //解析请求头信息
        int newIndex = sb.indexOf(end,lastIndex);
        while (lastIndex != newIndex){
            int flag = sb.indexOf(":",lastIndex);
            //截取请求头
            headerMap.put(sb.substring(lastIndex,flag),sb.substring(flag + 2,newIndex));
            lastIndex = newIndex + 2;
            newIndex = sb.indexOf(end,lastIndex);
        }
        newIndex += 2;
        //解析请求头
        if (newIndex < sb.length()){
            this.body = sb.substring(newIndex);
            String s = headerMap.get(HeaderUtil.CONTENT_TYPE_NAME);
            //post请求中body数据是xxx=xxx&xxx=xxx格式
            if (isPost && s != null && "application/x-www-from-urlencoded".equals(s)){
                //解析到parameterMap中
                parseUrlParameter(newIndex,sb.length());
            }
        }
    }

    public StringBuilder getSb() {
        return sb;
    }

    public void setSb(StringBuilder sb) {
        this.sb = sb;
    }

    public static String getEnd() {
        return end;
    }

    public static void setEnd(String end) {
        RequestStringParser.end = end;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }
}
