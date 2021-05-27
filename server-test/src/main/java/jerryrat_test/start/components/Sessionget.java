package jerryrat_test.start.components;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.container.Component;
import fun.jexing.container.HttpSession;

import java.io.PrintWriter;

/**
 * session测试
 */
@HttpComponent(url = "/get")
public class Sessionget implements Component {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpSession session = request.getSession();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (session == null){
            response.setStatus(200);
            response.setMsg("session是空的!");
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.finishResponse();
            return;
        }
        //获取session
        Object hello = session.get("hello");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(response.getFinishResult());
        writer.flush();
        writer.println("{ \"sessionVal\" : \""+hello+"\" }");
        writer.flush();
    }
}
