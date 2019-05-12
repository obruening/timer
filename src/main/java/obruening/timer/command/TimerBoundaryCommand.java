package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.TimerBoundaryEvent;

@Component
@Order(2)
public class TimerBoundaryCommand extends BaseCommand {
    
    @Override
    public String getName() {

        return "Timer Boundary";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new TimerBoundaryEvent());
    }
}
