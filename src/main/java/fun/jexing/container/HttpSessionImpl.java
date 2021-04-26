package fun.jexing.container;

import java.util.concurrent.ConcurrentHashMap;

public class HttpSessionImpl implements HttpSession {

    private ConcurrentHashMap<String,Object> map;

    HttpSessionImpl(){
        this.map = new ConcurrentHashMap<>();
    }
    @Override
    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public void set(String key, Object object) {
        map.put(key,object);
    }
}
