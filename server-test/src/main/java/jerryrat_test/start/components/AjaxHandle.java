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
        System.out.println(request.getRequestBody());
        System.out.println(request.getHeader("Content-Type"));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setMsg("{\"username\":\" "+username+"\" ," +
                " \"name\":\"zhangsan\"," +
                "\"age\": 18}");
        response.finishResponse();
    }
}
