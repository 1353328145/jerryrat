package fun.jexing.container;

public interface HttpSession {
    public Object get(String key);
    public void set(String key,Object object);
}
