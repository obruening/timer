package obruening.timer.workflow;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SimpleServiceTask implements TaskListener {
    
    private static Logger logger = LoggerFactory.getLogger(SimpleServiceTask.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        
        logger.info("i'm the simple service task");
    }
}
