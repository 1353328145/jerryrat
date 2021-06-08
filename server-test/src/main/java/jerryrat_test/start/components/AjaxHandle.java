package jerryrat_test.start.components;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.container.Component;

@HttpComponent(url = "/loadUser")
public class AjaxHandle implements Component {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String username = request.getParameter("username");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setMsg("{\"username\":\" "+username+"\" }");
        response.finishResponse();
    }
}
