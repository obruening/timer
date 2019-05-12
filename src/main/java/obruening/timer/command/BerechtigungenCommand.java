package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.BerechtigungenEvent;

@Component
@Order(6)
public class BerechtigungenCommand extends BaseCommand {
    
    @Override
    public String getName() {

        return "Berechtigungen";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new BerechtigungenEvent());
    }
}
