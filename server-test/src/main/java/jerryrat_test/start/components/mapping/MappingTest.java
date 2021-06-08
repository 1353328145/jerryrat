package jerryrat_test.start.components.mapping;

import fun.jexing.annotation.HttpHandleClass;
import fun.jexing.annotation.HttpMapping;
import fun.jexing.connector.HttpRequest;
import fun.jexing.connector.HttpResponse;

@HttpHandleClass
public class MappingTest {


    @HttpMapping(url = "/get")
    public void test(HttpRequest request,HttpResponse response){
        response.setMsg("{\"name\":\"张三\",\"password\":\"123456\"}");
        response.setContentType("application/j  son; charset=UTF-8");
        response.finishResponse();
    }
}
