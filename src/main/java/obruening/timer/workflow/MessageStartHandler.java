package obruening.timer.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageStartHandler implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(MessageStartHandler.class);
    
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        
        logger.info(String.format("execution id: %s", execution.getId()));
    }

}
