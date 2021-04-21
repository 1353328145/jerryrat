package fun.jexing.connector;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

public class Response implements HttpResponse{
    private Request request;
    private OutputStream output;
    Response(OutputStream output){
        this.output = output;
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

    public PrintWriter getWriter() throws IOException {
        return null;
    }

    public void setCharacterEncoding(String var1) {

    }

    public void setContentLength(int var1) {

    }

    public void setContentType(String var1) {

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
}
