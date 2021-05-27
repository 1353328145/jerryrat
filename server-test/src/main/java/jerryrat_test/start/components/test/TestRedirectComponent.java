package jerryrat_test.start.components.test;

import fun.jexing.annotation.HttpComponent;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;
import fun.jexing.container.Component;

/**
 * 转发测试
 */
@HttpComponent(url = "/hello")
public class TestRedirectComponent implements Component {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        response.redirect("/get");
    }
}
