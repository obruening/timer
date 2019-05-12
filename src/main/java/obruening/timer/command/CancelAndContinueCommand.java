package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.CancelAndContinueEvent;

@Component
@Order(4)
public class CancelAndContinueCommand extends BaseCommand {
    
    @Override
    public String getName() {

        return "Cancel And Continue";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new CancelAndContinueEvent());
    }
}
