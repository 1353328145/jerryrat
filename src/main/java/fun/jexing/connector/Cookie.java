package fun.jexing.connector;

import fun.jexing.utils.DateTool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cookie {
    //键
    private String name;
    //值
    private String value;
    //失效时间
    private Date expires;
    //域名
    private String domain;
    //路径
    private String path;
    //安全才发送
    private boolean secure;
    //是否允许js访问
    private boolean httpOnly;

    public Cookie(String name, String value) {
        if (this.isToken(name) && !name.equalsIgnoreCase("Comment") && !name.equalsIgnoreCase("Discard") && !name.equalsIgnoreCase("Domain") && !name.equalsIgnoreCase("Expires") && !name.equalsIgnoreCase("Max-Age") && !name.equalsIgnoreCase("Path") && !name.equalsIgnoreCase("Secure") && !name.equalsIgnoreCase("Version")) {
            this.name = name;
            this.value = value;
        }else {
            throw new IllegalArgumentException("cookie参数异常");
        }
    }
    private boolean isToken(String value) {
        int len = value.length();
        for(int i = 0; i < len; ++i) {
            char c = value.charAt(i);
            if (c < ' ' || c >= 127 || ",;".indexOf(c) != -1) {
                return false;
            }
        }
        return true;
    }
    public static Cookie[] parseCookieHeader(String header) {
        if ((header == null) || (header.length() < 1)){
            return (new Cookie[0]);
        }
        List<Cookie> cookies = new ArrayList<Cookie>();
        while (header.length() > 0) {
            int semicolon = header.indexOf(';');
            if (semicolon < 0) {
                semicolon = header.length();
            }
            if (semicolon == 0){break;}
            String token = header.substring(0, semicolon);
            if (semicolon < header.length()){
                header = header.substring(semicolon + 1);
            } else{
                header = "";
            }
            int equals = token.indexOf('=');
            if (equals > 0) {
                String name = token.substring(0, equals).trim();
                String value = token.substring(equals+1).trim();
                cookies.add(new Cookie(name, value));
            }
        }
        return cookies.toArray(new Cookie[cookies.size()]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value);
        if (expires!=null){
            sb.append("; ").append(DateTool.getDateString(expires));
        }
        if (domain != null){
            sb.append("; ").append(domain);
        }
        if (path!=null){
            sb.append("; ").append(path);
        }
        if (secure){
            sb.append("; ").append("Secure");
        }
        if (httpOnly){
            sb.append("; ").append("HttpOnly");
        }
        return sb.toString();
    }
}
