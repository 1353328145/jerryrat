package jerryrat_test.start.components.test;


import fun.jexing.annotation.HttpComponent;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.container.Component;

/**
 * 测试转发
 */
@HttpComponent(url = "/forward")
public class TestForwardComponent implements Component {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        request.forward("/indeasdasx.html",request,response);
    }
}
