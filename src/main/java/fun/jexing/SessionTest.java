package fun.jexing;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.container.Component;
import fun.jexing.container.HttpSession;

import java.io.PrintWriter;

@HttpComponent(url = "/sessionVal")
public class SessionTest implements Component {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        HttpSession session = request.getSession();
        PrintWriter writer = response.getWriter();
        writer.print(response.getFinishResult());
        writer.flush();
        writer.print("{ \"code\" : \"200\" , \"msg\" : \""+session.get("hello")+"\" }");
        writer.flush();
    }
}
