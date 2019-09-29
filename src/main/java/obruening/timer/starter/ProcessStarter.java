package obruening.timer.starter;

import java.util.Map;

public interface ProcessStarter {
    
    Map<String, Object> getMap();
    
    void start(Map<String, Object> map);
    
    String getKey();
}
