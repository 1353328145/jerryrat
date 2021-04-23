package fun.jexing.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public interface HttpRequest {
    default String getCharacterEncoding(){
        return "UTF-8";
    }

    int getContentLength();

    String getContentType();

    String getParameter(String var1);

    Set<String> getParameterNames();

    String getProtocol();

    int getServerPort();

    String getRemoteAddr();

    String getRemoteHost();

    int getRemotePort();

    String getMethod();

    String getRequestURI();

    String getHeader(String name);

    Set<String> getHeaderNames();
}
