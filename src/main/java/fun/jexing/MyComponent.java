package fun.jexing;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.container.Component;
import fun.jexing.container.HttpSession;

import java.io.PrintWriter;

@HttpComponent(url = "/myComponent")
public class MyComponent implements Component {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        HttpSession session = request.getSession();
        session.set("hello","world");
        PrintWriter writer = response.getWriter();
        writer.print(response.getFinishResult());
        writer.flush();
        writer.print("{ \"code\" : \"200\" , \"msg\" : \"hello world\" }");
        writer.flush();
    }
}
