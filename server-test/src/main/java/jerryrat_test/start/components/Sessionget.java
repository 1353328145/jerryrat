package jerryrat_test.start.components;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.container.Component;
import fun.jexing.container.HttpSession;

import java.io.PrintWriter;

@HttpComponent(url = "/get")
public class Sessionget implements Component {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpSession session = request.getSession();
        //获取session
        Object hello = session.get("hello");
        PrintWriter writer = response.getWriter();
        writer.print(response.getFinishResult());
        writer.flush();
        writer.println("{ \"sessionVal\" : \""+hello+"\" }");
        writer.flush();
    }
}
