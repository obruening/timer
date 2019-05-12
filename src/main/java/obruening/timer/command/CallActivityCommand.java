package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.CallActivityEvent;

@Component
@Order(5)
public class CallActivityCommand extends BaseCommand {
    
    @Override
    public String getName() {

        return "Call Activity";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new CallActivityEvent());
    }
}
