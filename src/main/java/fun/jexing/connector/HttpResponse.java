package fun.jexing.connector;

import java.io.PrintWriter;

public interface HttpResponse {
    /**
     * 将响应设置的数据，转化为http报文格式
     * @return
     */
    StringBuffer getFinishResult();

    /**
     * 设置状态码
     * @param status
     */
    HttpResponse setStatus(int status);

    /**
     * 设置body数据
     * @param msg
     */
    HttpResponse setMsg(String msg);

    /**
     * 获取编码格式
     * @return
     */
    String getCharacterEncoding();

    /**
     * 获取编码类型
     * @return
     */
    String getContentType();

    /**
     * 获取打印流
     * @return
     */
    PrintWriter getWriter();

    /**
     * 设置编码类型
     * @param var1
     */
    HttpResponse setCharacterEncoding(String var1);

    /**
     * 设置响应数据长度
     * @param var1
     */
    HttpResponse setContentLength(int var1);
    /**
     * 设置编码格式
     * @return
     */
    HttpResponse setContentType(String var1);

    /**
     * 设置响应头
     * @return
     */
    HttpResponse setHeader(String key,String value);
    /**
     * 向客户端相应数据
     */
    void finishResponse();
    /**
     * 重定向
     * @param url 重定向路径
     */
    void redirect(String url);

}
