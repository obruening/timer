package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.MultiTimerEvent;

@Component
@Order(3)
public class MultiTimerCommand extends BaseCommand {
    
    @Override
    public String getName() {

        return "Multi Timer";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new MultiTimerEvent());
    }
}
