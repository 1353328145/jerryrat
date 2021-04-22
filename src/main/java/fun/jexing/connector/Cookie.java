package fun.jexing.connector;

import java.util.ArrayList;
import java.util.List;

public class Cookie {
    private String name;
    private String value;
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
}
