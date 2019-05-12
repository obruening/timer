package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.TimerIntermediateEvent;

@Component
@Order(1)
public class TimerIntermediateCommand extends BaseCommand {
    
    @Override
    public String getName() {

        return "Timer Intermediate";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new TimerIntermediateEvent());
    }
}
