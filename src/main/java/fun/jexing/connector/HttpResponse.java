package fun.jexing.connector;

import java.io.PrintWriter;

public interface HttpResponse {
    //获取数据之外的其他信息
    public StringBuffer getFinishResult();

    void setStatus(int status);

    void setMsg(String msg);

    String getCharacterEncoding();

    String getContentType();

    PrintWriter getWriter();

    void setCharacterEncoding(String var1);

    void setContentLength(int var1);

    void setContentType(String var1);
}
