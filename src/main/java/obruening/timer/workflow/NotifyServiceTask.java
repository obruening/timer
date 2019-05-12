package obruening.timer.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import obruening.timer.store.event.TimerTriggeredEvent;

@Component
public class NotifyServiceTask implements JavaDelegate, TaskListener {
    
    private static Logger logger = LoggerFactory.getLogger(NotifyServiceTask.class);
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(DelegateExecution execution) throws Exception {

        //applicationEventPublisher.publishEvent(new TimerTriggeredEvent());
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        
        /*
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
          @Override
          public void run() {
              System.out.println("-----------> scheduled update");
              applicationEventPublisher.publishEvent(new UpdateEvent());
          }
        }, 2, TimeUnit.SECONDS);
        */
        
        
        logger.info("-----timer triggered");
        applicationEventPublisher.publishEvent(new TimerTriggeredEvent());
    }

}
